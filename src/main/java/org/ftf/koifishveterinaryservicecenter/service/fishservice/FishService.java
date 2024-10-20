package org.ftf.koifishveterinaryservicecenter.service.fishservice;


import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FishService {
    List<Fish> getAllFishByUserId(int Id);

    FishDTO getDetailFish(int fishId);

    Fish getFishById(Integer fishId);

    ImageDTO addImageForFish(int fishId, String sourcePath);

    FishDTO updateFish(Integer fishId, FishDTO fishDTO);

    FishDTO removeFish(int fishID, boolean enabled);

    ImageDTO removeImage(int imageID, boolean enabled);
    FishDTO addFish(FishDTO fishDTO);

}
