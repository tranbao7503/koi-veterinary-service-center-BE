package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FishService {
    List<Fish> getAllFishByUserId(int Id);

}
