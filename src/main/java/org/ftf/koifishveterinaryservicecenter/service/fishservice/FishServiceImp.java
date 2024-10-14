package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;

    public FishServiceImp(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
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
}
