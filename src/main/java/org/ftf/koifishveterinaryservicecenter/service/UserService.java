package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
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

    // Convert User entity to UserDto
    public UserDto convertToUserDTO(User user) {
        TypeMap<User, UserDto> typeMap = modelMapper.createTypeMap(User.class, UserDto.class);

        return typeMap.map(user);
    }

    // Get all customers with role id 2 and convert them to UserDto
    public List<UserDto> getAllCustomers() {
        List<User> users = userRepository.findAllByRoleRoleId(2);
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
}
