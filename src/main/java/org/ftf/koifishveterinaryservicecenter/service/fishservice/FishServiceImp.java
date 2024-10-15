package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.enums.Gender;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Fish> getAllFishByUserId(int id) {
        // Lấy danh sách tất cả các con cá thuộc về customer có ID tương ứng
        List<Fish> allFish = fishRepository.findAllFishByCustomer_UserId(id);

        // Lọc danh sách cá, chỉ giữ lại những con có enabled = true
        return allFish.stream()
                .filter(fish -> fish.isEnabled())  // Sử dụng phương thức getEnabled() để lọc
                .collect(Collectors.toList());
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
                return fishMapper.convertEntityToDto(fish);
            } else {
                // Trả về 406 nếu không có quyền truy cập
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to access this fish.");
            }
        } else {
            return null; // Trả về null nếu cá không tồn tại hoặc không được kích hoạt
        }
    }



}


