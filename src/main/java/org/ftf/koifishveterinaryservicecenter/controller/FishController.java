package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/addfish")
    public ResponseEntity<FishDTO> addFish(@RequestBody FishDTO fishDTO) {
        FishDTO createdFish = fishService.addFish(fishDTO);
        if (createdFish == null) {
            return ResponseEntity.badRequest().build(); // Trả về 400 nếu không thể thêm
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFish); // Trả về cá đã tạo với mã 201
        }
    }
}
