package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.springframework.stereotype.Service;

@Service
public interface FishService {
    FishDTO addFish(FishDTO fishDTO);

}
