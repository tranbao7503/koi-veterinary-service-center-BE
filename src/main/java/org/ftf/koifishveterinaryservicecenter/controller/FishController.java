package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fish")
public class FishController {
    private final FishService fishService;
    private final FishMapper fishMapper;

    @Autowired
    public FishController(FishService fishService, FishMapper fishMapper) {
        this.fishService = fishService;
        this.fishMapper = fishMapper;

    }

    @PutMapping("update/{fishId}")
    public ResponseEntity<FishDTO> updateFish(
            @PathVariable Integer fishId,
            @RequestBody FishDTO fishDTO) {
        FishDTO updatedFish = fishService.updateFish(fishId, fishDTO);
        return ResponseEntity.ok(updatedFish);
    }
}
