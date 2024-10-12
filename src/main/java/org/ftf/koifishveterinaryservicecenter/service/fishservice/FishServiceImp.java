package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;
    private final FishMapper fishMapper;
    private final AuthenticationServiceImpl authenticationService;

    public FishServiceImp(FishRepository fishRepository, FishMapper fishMapper, AuthenticationServiceImpl authenticationService) {
        this.fishRepository = fishRepository;
        this.fishMapper = fishMapper;
        this.authenticationService = authenticationService;
    }


    @Override
    public FishDTO getDetailFish(int fishId) {
        Fish fish = fishRepository.findByFishId(fishId);
        return fish != null ? fishMapper.convertEntityToDto(fish) : null;
    }


    @Override
    public List<Fish> getAllFishByUserId(int Id) {
        return fishRepository.findAllFishByCustomer_UserId(Id);
    }

    //Xoa ca khoi list
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
}
