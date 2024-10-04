package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /*
     * Convert Service entity to Service DTO
     * */
    public ServiceDTO convertToServiceDTO(Service service) {
        return modelMapper.map(service, ServiceDTO.class);
    }

    /*
     * Convert Service DTO to Service entity
     * */
    public Service convertToService(ServiceDTO serviceDTO) {
        return modelMapper.map(serviceDTO, Service.class);
    }

    /*
     * Convert User entity to User DTO
     * */
    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /*
     * Convert User DTO to User entity
     * */
    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
