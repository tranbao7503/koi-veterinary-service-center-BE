package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.exception.FishNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            // Gọi phương thức updateFish từ service
            FishDTO updatedFish = fishService.updateFish(fishId, fishDTO);

            // Trả về kết quả thành công
            return ResponseEntity.ok(updatedFish);
        } catch (FishNotFoundException e) {
            // Trả về mã 404 nếu không tìm thấy cá
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }
}
