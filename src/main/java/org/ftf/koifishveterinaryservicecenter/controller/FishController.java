package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.service.fishservice.FishService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fish")
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

   /* @GetMapping("/listfish")
    public ResponseEntity<?> getListFishes(@RequestParam("userId") int userId) {

        // Gọi service để lấy danh sách cá theo userId
        List<Fish> fishes = fishService.getAllFishByUserId(userId);

        // Kiểm tra nếu danh sách rỗng
        if (fishes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Chuyển đổi danh sách Fish sang FishDTO
            List<FishDTO> fishDTOs = fishes.stream()
                    .map(fishMapper::convertEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(fishDTOs, HttpStatus.OK);
        }
    }*/

    @GetMapping("/listfish")
    public ResponseEntity<?> getListFishes(@RequestHeader("Authorization") String authorizationHeader) throws ParseException {
        // Loại bỏ tiền tố "Bearer " từ Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Gọi hàm getUserInfoFromToken để lấy userId từ token
        IntrospectResponse introspectResponse = authenticationService.getUserInfoFromToken(new IntrospectRequestDTO(token));

        // Kiểm tra introspectResponse có bị null hay không
        if (introspectResponse == null) {
            return new ResponseEntity<>("Invalid token or user information", HttpStatus.UNAUTHORIZED);
        }

        int userId = introspectResponse.getUserId();

        // Gọi service để lấy danh sách cá theo userId
        List<Fish> fishes = fishService.getAllFishByUserId(userId);

        // Kiểm tra nếu danh sách rỗng
        if (fishes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Chuyển đổi danh sách Fish sang FishDTO
            List<FishDTO> fishDTOs = fishes.stream()
                    .map(fishMapper::convertEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(fishDTOs, HttpStatus.OK);
        }
    }

}
