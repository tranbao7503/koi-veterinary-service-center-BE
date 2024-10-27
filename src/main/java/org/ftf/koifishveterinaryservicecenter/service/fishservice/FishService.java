package org.ftf.koifishveterinaryservicecenter.service.fishservice;


import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FishService {
    List<Fish> getAllFishByUserId(int Id);

    FishDTO getDetailFish(int fishId);

    Fish getFishById(Integer fishId);


    FishDTO updateFish(Integer fishId, FishDTO fishDTO);


    ImageDTO addImageForFish(int fishId, MultipartFile image) throws Exception;

    FishDTO removeFish(int fishID, boolean enabled);

    ImageDTO removeImage(int imageID, boolean enabled);

    FishDTO addFish(FishDTO fishDTO);

}
