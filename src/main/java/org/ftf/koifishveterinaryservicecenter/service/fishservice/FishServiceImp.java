package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.entity.Image;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.Gender;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.ImageNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.ImageMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.repository.ImageRepository;
import org.ftf.koifishveterinaryservicecenter.service.fileservice.FileUploadService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FishServiceImp implements FishService {

    private final FishMapper fishMapper;
    private final AuthenticationServiceImpl authenticationService;
    private final FishRepository fishRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final FileUploadService fileUploadService;

    public FishServiceImp(FishMapper fishMapper, AuthenticationServiceImpl authenticationService, FishRepository fishRepository, ImageRepository imageRepository, ImageMapper imageMapper, FileUploadService fileUploadService) {
        this.fishMapper = fishMapper;
        this.authenticationService = authenticationService;
        this.fishRepository = fishRepository;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.fileUploadService = fileUploadService;
    }


    public List<FishDTO> getAllFishByToken(String authorizationHeader) {
        // Loại bỏ tiền tố "Bearer " từ Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Gọi hàm getUserInfoFromToken để lấy userId từ token
        IntrospectResponse introspectResponse = null;
        try {
            introspectResponse = authenticationService.getUserInfoFromToken(new IntrospectRequestDTO(token));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Kiểm tra introspectResponse có bị null hay không
        if (introspectResponse == null) {
            throw new AuthenticationException("Invalid token or user information");
        }

        int userId = introspectResponse.getUserId();

        // Lấy danh sách tất cả các con cá thuộc về customer có ID tương ứng
        List<Fish> allFish = fishRepository.findAllFishByCustomer_UserId(userId);

        // Lọc danh sách cá, chỉ giữ lại những con có enabled = true
        List<Fish> enabledFish = allFish.stream()
                .filter(Fish::isEnabled)
                .collect(Collectors.toList());

        // Chuyển đổi danh sách Fish sang FishDTO
        return enabledFish.stream()
                .map(fishMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FishDTO updateFish(Integer fishId, FishDTO fishDTO) {
        // Lấy userId từ token của người dùng đang đăng nhập
        Integer loggedInCustomerId = authenticationService.getAuthenticatedUserId();

        // Lấy thông tin cá từ database
        Fish fishFromDb = fishRepository.findByFishId(fishId).orElse(null);
        if (fishFromDb == null) {
            throw new FishNotFoundException("Fish not found with id: " + fishId);
        }

        // Kiểm tra xem con cá có thuộc về customer đang đăng nhập không
        if (!fishFromDb.getCustomer().getUserId().equals(loggedInCustomerId)) {
            throw new AuthenticationException("You do not have permission to update this fish.");
        }

        // Cập nhật thông tin cá
        fishFromDb.setGender(Gender.valueOf(fishDTO.getGender()));
        fishFromDb.setAge(fishDTO.getAge());
        fishFromDb.setSpecies(fishDTO.getSpecies());
        fishFromDb.setSize(BigDecimal.valueOf(fishDTO.getSize()));
        fishFromDb.setWeight(BigDecimal.valueOf(fishDTO.getWeight()));
        fishFromDb.setColor(fishDTO.getColor());
        fishFromDb.setOrigin(fishDTO.getOrigin());

        // Lưu cá đã cập nhật
        Fish updatedFish = fishRepository.save(fishFromDb);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return fishMapper.convertEntityToDto(updatedFish);
    }


    @Override
    public FishDTO getDetailFish(int fishId) {
        Fish fish = fishRepository.findByFishId(fishId);

        // Kiểm tra cá có tồn tại và có trạng thái enabled = true hay không
        if (fish != null && fish.isEnabled()) {
            // Lấy userId từ token
            int userId = authenticationService.getAuthenticatedUserId();

            // So sánh userId với customerId của con cá
            if (fish.getCustomer().getUserId() == userId) {

                // Lấy danh sách hình ảnh từ ImageRepository dựa trên fishId
                List<Image> images = imageRepository.findByFishFishId(fishId);

                // Sử dụng ImageMapper để chuyển đổi danh sách Image thành danh sách ImageDTO
                List<ImageDTO> imageDTOs = images.stream()
                        .map(imageMapper::convertEntityToDto) // Sử dụng ImageMapper
                        .collect(Collectors.toList());

                // Chuyển đổi Fish entity thành FishDTO
                FishDTO fishDTO = fishMapper.convertEntityToDto(fish);

                // Gán danh sách hình ảnh vào FishDTO
                fishDTO.setImages(imageDTOs);

                return fishDTO;
            } else {
                // Trả về 406 nếu không có quyền truy cập
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to access this fish.");
            }
        } else {
            return null; // Trả về null nếu cá không tồn tại hoặc không được kích hoạt
        }
    }

    @Override
    public Fish getFishById(Integer fishId) {
        Optional<Fish> fish = fishRepository.findById(fishId);
        if (fish.isEmpty()) throw new FishNotFoundException("Fish not found with id " + fishId);
        return fish.get();
    }


    @Override
    public ImageDTO addImageForFish(int fishId, MultipartFile image) throws IOException {
        Fish fish = fishRepository.findByFishId(fishId);

        // Kiểm tra nếu fish không tồn tại
        if (fish == null) {
            throw new RuntimeException("Fish not found with id: " + fishId);
        }

        // Lấy customerId từ fish
        int fishCustomerId = fish.getCustomer().getUserId();

        // Lấy customerId từ token
        int loggedInCustomerId = authenticationService.getAuthenticatedUserId(); // Sử dụng phương thức của bạn

        // So sánh customerId của fish với customerId của người dùng đăng nhập
        if (fishCustomerId != loggedInCustomerId) {
            throw new AuthenticationException("You can only add images for your own fish.");
        }

        // Upload file và nhận về đường dẫn của file
        String path = fileUploadService.uploadFile(image);

        // Tạo đối tượng Image mới
        Image newImage = new Image();
        newImage.setSourcePath(path); // Đường dẫn file
        newImage.setFish(fish); // Gán fish cho image

        // Lưu Image vào cơ sở dữ liệu
        Image savedImage = imageRepository.save(newImage);

        // Sử dụng mapper để chuyển đổi từ Image entity sang ImageDTO
        return imageMapper.convertEntityToDto(savedImage);
    }


    @Override
    public ImageDTO removeImage(int imageID, boolean enabled) {
        // Tìm kiếm ảnh từ database
        Image imageFromDb = imageRepository.findById(imageID).orElse(null);

        if (imageFromDb == null) {
            throw new ImageNotFoundException("Image not found with ID: " + imageID);
        }

        // Lấy fishId từ ảnh
        int fishId = imageFromDb.getFish().getFishId();

        // Tìm con cá từ fishId
        Fish fishFromDb = fishRepository.findById(fishId).orElse(null);

        if (fishFromDb == null) {
            throw new FishNotFoundException("Fish not found with ID: " + fishId);
        }

        // Lấy customerId từ fish
        int fishCustomerId = fishFromDb.getCustomer().getUserId();

        // Lấy customerId từ token
        int loggedInCustomerId = authenticationService.getAuthenticatedUserId(); // Sử dụng phương thức của bạn

        // So sánh customerId của fish với customerId của người dùng đăng nhập
        if (fishCustomerId != loggedInCustomerId) {
            throw new AuthenticationException("You can only disable images of your own fish.");
        }

        // Cập nhật enable/disable cho image
        imageFromDb.setEnabled(enabled);

        // Lưu ảnh đã cập nhật
        Image updatedImage = imageRepository.save(imageFromDb);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return imageMapper.convertEntityToDto(updatedImage);
    }

    @Override
    public FishDTO removeFish(int fishId, boolean enabled) {
        // Lấy thông tin con cá từ database
        Fish fishFromDb = fishRepository.findById(fishId).orElse(null);

        if (fishFromDb == null) {
            throw new FishNotFoundException("Fish not found with ID: " + fishId);
        }

        // Lấy customerId từ fish
        int fishCustomerId = fishFromDb.getCustomer().getUserId();

        // Lấy customerId từ token
        int loggedInCustomerId = authenticationService.getAuthenticatedUserId(); // Sử dụng phương thức của bạn

        // So sánh customerId của fish với customerId của người dùng đăng nhập
        if (fishCustomerId != loggedInCustomerId) {
            throw new AuthenticationException("You can only remove your own fish.");
        }

        // Cập nhật enable/disable cho fish
        fishFromDb.setEnabled(enabled);

        // Lưu cá đã cập nhật
        Fish updatedFish = fishRepository.save(fishFromDb);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return fishMapper.convertEntityToDto(updatedFish);
    }

    public FishDTO addFish(FishDTO fishDTO) {
        // Lấy customerId từ token
        int customerId = authenticationService.getAuthenticatedUserId();  // Lấy customerId từ token

        // Chuyển đổi từ FishDTO sang Fish
        Fish fish = fishMapper.convertDtoToEntity(fishDTO);

        // Tạo một đối tượng User và gán customerId cho User
        User customer = new User();
        customer.setUserId(customerId);  // Gán customerId vào đối tượng User

        // Gán đối tượng User (customer) vào Fish
        fish.setCustomer(customer);

        // Lưu đối tượng Fish vào repository
        Fish savedFish = fishRepository.save(fish);

        // Sử dụng MapStruct để chuyển đổi từ Fish sang FishDTO và trả về
        return fishMapper.convertEntityToDto(savedFish);
    }


}
