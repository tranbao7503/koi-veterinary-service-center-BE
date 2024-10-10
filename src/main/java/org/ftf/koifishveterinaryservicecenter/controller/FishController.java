package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/fishdetail/{fishId}")
    public ResponseEntity<FishDTO> getFishDetail(@PathVariable int fishId) {
        FishDTO fishDTO = fishService.getDetailFish(fishId);
        if (fishDTO != null) {
            return ResponseEntity.ok(fishDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
