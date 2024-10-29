package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fishes")
public class FishController {
    private final FishService fishService;
    private final FishMapper fishMapper;
    private final AuthenticationService authenticationService;


    @Autowired
    public FishController(FishService fishService, FishMapper fishMapper, AuthenticationService authenticationService) {
        this.fishService = fishService;
        this.fishMapper = fishMapper;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/{fishId}")
    public ResponseEntity<FishDTO> getFishDetail(@PathVariable int fishId) {
        FishDTO fishDTO = fishService.getDetailFish(fishId);
        if (fishDTO != null) {
            return ResponseEntity.ok(fishDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<List<FishDTO>> getListFishes(@RequestHeader("Authorization") String authorizationHeader) {
        // Gọi service để lấy danh sách cá theo token
        List<FishDTO> fishDTOs = fishService.getAllFishByToken(authorizationHeader);

        // Kiểm tra nếu danh sách rỗng
        if (fishDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(fishDTOs, HttpStatus.OK);
        }
    }

}

