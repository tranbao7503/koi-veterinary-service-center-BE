package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;

    public FishServiceImp(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }


    @Override
    public List<Fish> getAllFishByUserId(int Id) {
        return fishRepository.findAllFishByCustomer_UserId(Id);
    }
}
