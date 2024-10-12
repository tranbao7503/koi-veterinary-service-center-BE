package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;
    private final FishMapper fishMapper;

    public FishServiceImp(FishRepository fishRepository, FishMapper fishMapper) {
        this.fishRepository = fishRepository;
        this.fishMapper = fishMapper;
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
        // Lấy thông tin người dùng từ database
        Fish fishFromDb = fishRepository.findById(fishId).orElse(null);


        if (fishFromDb == null) {
            throw new FishNotFoundException("User not found with ID: " + fishId);
        }

        // Cập nhật enable/disable cho fish
        fishFromDb.setEnabled(enabled);

        // Lưu người dùng đã cập nhật
        Fish updatedFish = fishRepository.save(fishFromDb);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return fishMapper.convertEntityToDto(updatedFish);
    }
}
