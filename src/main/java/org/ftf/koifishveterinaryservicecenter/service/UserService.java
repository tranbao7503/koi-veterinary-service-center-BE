package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
    public User convertToUserDTO(User user) {
        TypeMap<User, UserDTO> typeMap = modelMapper.getTypeMap(User.class, UserDTO.class);
//        typeMap.addMappings(mapper -> mapper.skip(User::getPassword));
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        }
        return typeMap.map(user);
    }

    /*
     * Convert Service DTO to Service entity
     * */
//    public User convertToUser(UserDTO userDTO) {
//        return modelMapper.map(userDTO, org.ftf.koifishveterinaryservicecenter.model.Service.class);
//    }

    public List<User> getAllCustomers() {
        List<User> users = userRepository.findAllByRoleRoleId(2);
        return users;
    }


}
