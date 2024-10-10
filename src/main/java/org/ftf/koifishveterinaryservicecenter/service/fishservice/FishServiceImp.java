package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.enums.Gender;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FishServiceImp implements FishService {

    private final FishMapper fishMapper;
    private final FishRepository fishRepository;

    public FishServiceImp(FishMapper fishMapper, FishRepository fishRepository) {
        this.fishMapper = fishMapper;
        this.fishRepository = fishRepository;
    }

    @Override
    public FishDTO updateFish(Integer fishId, FishDTO fishDTO) {
        // Lấy thông tin cá từ database
        Fish fishFromDb = fishRepository.findByFishId(fishId).orElse(null);
        if (fishFromDb == null) {
            throw new FishNotFoundException("Fish not found with id: " + fishId);
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
