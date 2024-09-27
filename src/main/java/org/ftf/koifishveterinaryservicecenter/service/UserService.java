package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToUserDTO(User user) {
        TypeMap<User, UserDTO> typeMap = modelMapper.getTypeMap(User.class, UserDTO.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        }
        return typeMap.map(user);
    }

    public List<UserDTO> getAllUser(int roleId) {
        List<User> users = userRepository.findAllByRoleRoleId(roleId);
        List<UserDTO> userDtos = users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        return userDtos;
    }
}
