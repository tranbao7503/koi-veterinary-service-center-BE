package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.entity.Image;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.ImageMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.repository.ImageRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;
    private final FishMapper fishMapper;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AuthenticationServiceImpl authenticationService;

    public FishServiceImp(FishRepository fishRepository, FishMapper fishMapper, ImageRepository imageRepository, ImageMapper imageMapper, AuthenticationServiceImpl authenticationService) {
        this.fishRepository = fishRepository;
        this.fishMapper = fishMapper;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.authenticationService = authenticationService;
    }


    @Override
    public FishDTO getDetailFish(int fishId) {
        Fish fish = fishRepository.findByFishId(fishId);
        return fish != null ? fishMapper.convertEntityToDto(fish) : null;
    }

    @Override
    public ImageDTO addImageForFish(int fishId, String sourcePath) {
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

        // Tạo một đối tượng Image mới
        Image image = new Image();
        image.setSourcePath(sourcePath);
        image.setFish(fish); // Thiết lập Fish cho Image

        // Lưu Image vào cơ sở dữ liệu
        Image savedImage = imageRepository.save(image);

        // Sử dụng mapper để chuyển đổi từ Image entity sang ImageDTO
        return imageMapper.convertEntityToDto(savedImage);
    }


    @Override
    public List<Fish> getAllFishByUserId(int Id) {
        return fishRepository.findAllFishByCustomer_UserId(Id);
    }
}
