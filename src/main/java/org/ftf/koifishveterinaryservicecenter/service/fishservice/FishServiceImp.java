package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Fish> getAllFishByUserId(int id) {
        // Lấy danh sách tất cả các con cá thuộc về customer có ID tương ứng
        List<Fish> allFish = fishRepository.findAllFishByCustomer_UserId(id);

        // Lọc danh sách cá, chỉ giữ lại những con có enabled = true
        return allFish.stream()
                .filter(fish -> fish.isEnabled())  // Sử dụng phương thức getEnabled() để lọc
                .collect(Collectors.toList());
    }

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

    @Override
    public Fish getFishById(Integer fishId) {
        Optional<Fish> fish = fishRepository.findById(fishId);
        if (fish.isEmpty()) throw  new FishNotFoundException("Fish not found with id " + fishId);
        return fish.get();
    }
}
