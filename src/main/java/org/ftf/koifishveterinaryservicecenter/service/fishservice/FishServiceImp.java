package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.springframework.stereotype.Service;

@Service
public class FishServiceImp implements FishService {

    private final FishMapper fishMapper;
    private final FishRepository fishRepository;

    public FishServiceImp(FishMapper fishMapper, FishRepository fishRepository) {
        this.fishMapper = fishMapper;
        this.fishRepository = fishRepository;
    }


    public FishDTO addFish(FishDTO fishDTO) {
        // Chuyển đổi từ FishDTO sang Fish
        Fish fish = fishMapper.convertDtoToEntity(fishDTO); // Giả sử fishMapper đã được cấu hình để chuyển đổi

        // Lưu cá vào repository
        Fish savedFish = fishRepository.save(fish);

        // Sử dụng MapStruct để chuyển đổi từ Fish sang FishDTO
        return fishMapper.convertEntityToDto(savedFish);
    }
}
