package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.entity.Image;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;
    private final FishMapper fishMapper;
    private final ImageRepository imageRepository;

    public FishServiceImp(FishRepository fishRepository, FishMapper fishMapper, ImageRepository imageRepository) {
        this.fishRepository = fishRepository;
        this.fishMapper = fishMapper;
        this.imageRepository = imageRepository;
    }


    @Override
    public FishDTO getDetailFish(int fishId) {
        Fish fish = fishRepository.findByFishId(fishId);
        return fish != null ? fishMapper.convertEntityToDto(fish) : null;
    }

    @Override
    public Image addImageForFish(int fishId, String sourcePath) {
        Fish fish = fishRepository.findByFishId(fishId);

        // Kiểm tra nếu fish không tồn tại
        if (fish == null) {
            throw new RuntimeException("Fish not found with id: " + fishId);
        }

        // Tạo một đối tượng Image mới
        Image image = new Image();
        image.setSourcePath(sourcePath);
        image.setFish(fish); // Thiết lập Fish cho Image

        // Lưu Image vào cơ sở dữ liệu
        return imageRepository.save(image);
    }


    @Override
    public List<Fish> getAllFishByUserId(int Id) {
        return fishRepository.findAllFishByCustomer_UserId(Id);
    }
}
