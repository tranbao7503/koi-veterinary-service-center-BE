package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    public ManagerService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    public UserDTO convertToUserDTO(User user) {
        TypeMap<User, UserDTO> typeMap = modelMapper.getTypeMap(User.class, UserDTO.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        }
        return typeMap.map(user);
    }

    public UserDTO createStaff(String userName, String passWord, String firstName, String lastName) {
        return createUser(userName, passWord, firstName, lastName, roleRepository.getReferenceById(4));
    }

    public UserDTO createVeterianrian(String userName, String passWord, String firstName, String lastName) {
        return createUser(userName, passWord, firstName, lastName, roleRepository.getReferenceById(3));
    }

    public UserDTO createUser(String userName, String passWord, String firstName, String lastName, Role role) {
        User user = new User();

        user.setUsername(userName);
        user.setPassword(passWord);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        //encrypt password

        userRepository.save(user);
        return convertToUserDTO(user);
    }
}
