package org.ftf.koifishveterinaryservicecenter.service.fishservice;

import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.mapper.FishMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FishRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FishServiceImp implements FishService {
    private final FishRepository fishRepository;
    private final FishMapper fishMapper;
    private final AuthenticationService authenticationService;

    public FishServiceImp(FishRepository fishRepository, FishMapper fishMapper, AuthenticationService authenticationService) {
        this.fishRepository = fishRepository;
        this.fishMapper = fishMapper;
        this.authenticationService = authenticationService;
    }


    @Override
    public List<FishDTO> getAllFishByToken(String authorizationHeader) {
        // Loại bỏ tiền tố "Bearer " từ Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Gọi hàm getUserInfoFromToken để lấy userId từ token
        IntrospectResponse introspectResponse = null;
        try {
            introspectResponse = authenticationService.getUserInfoFromToken(new IntrospectRequestDTO(token));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Kiểm tra introspectResponse có bị null hay không
        if (introspectResponse == null) {
            throw new AuthenticationException("Invalid token or user information");
        }

        int userId = introspectResponse.getUserId();

        // Lấy danh sách tất cả các con cá thuộc về customer có ID tương ứng
        List<Fish> allFish = fishRepository.findAllFishByCustomer_UserId(userId);

        // Lọc danh sách cá, chỉ giữ lại những con có enabled = true
        List<Fish> enabledFish = allFish.stream()
                .filter(Fish::isEnabled)
                .collect(Collectors.toList());

        // Chuyển đổi danh sách Fish sang FishDTO
        return enabledFish.stream()
                .map(fishMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public FishDTO getDetailFish(int fishId) {
        Fish fish = fishRepository.findByFishId(fishId);
        return fish != null ? fishMapper.convertEntityToDto(fish) : null;
    }
}
