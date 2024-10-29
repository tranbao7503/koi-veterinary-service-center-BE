package org.ftf.koifishveterinaryservicecenter.service.fishservice;


import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;

import java.util.List;

public interface FishService {


    List<FishDTO> getAllFishByToken(String authorizationHeader);

    FishDTO getDetailFish(int fishId);

}
