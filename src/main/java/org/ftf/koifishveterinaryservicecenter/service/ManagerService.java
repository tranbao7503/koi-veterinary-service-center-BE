package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ManagerService(UserRepository userRepository, ModelMapper modelMapper) {
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

    public UserDTO updateUser(int userId,String passWord, String firstName, String lastName) {
        //kiem tra thong tin cu va thong tin moi
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        user.setPassword(passWord);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        //encrypt password
        userRepository.save(user);
        return convertToUserDTO(user);
    }


}
