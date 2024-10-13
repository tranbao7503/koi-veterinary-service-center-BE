package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.entity.Image;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.ImageNotFoundException;
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
    public List<Fish> getAllFishByUserId(int Id) {
        return fishRepository.findAllFishByCustomer_UserId(Id);
    }
}
