package org.ftf.koifishveterinaryservicecenter.service.fishservice;


import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;

import java.util.List;

public interface FishService {
    List<Fish> getAllFishByUserId(int Id);

    FishDTO getDetailFish(int fishId);

    ImageDTO removeImage(int imageID, boolean enabled);

}
