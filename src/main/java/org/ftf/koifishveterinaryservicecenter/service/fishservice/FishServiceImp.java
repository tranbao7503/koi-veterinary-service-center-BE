package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.enums.Gender;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FishServiceImp implements FishService {

    private final FishMapper fishMapper;
    private final FishRepository fishRepository;
    private final AuthenticationServiceImpl authenticationService;


    public FishServiceImp(FishMapper fishMapper, FishRepository fishRepository, AuthenticationServiceImpl authenticationService) {
        this.fishMapper = fishMapper;
        this.fishRepository = fishRepository;
        this.authenticationService = authenticationService;
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
}
