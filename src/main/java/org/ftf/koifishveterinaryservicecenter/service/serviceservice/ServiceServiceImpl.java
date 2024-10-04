package org.ftf.koifishveterinaryservicecenter.service.serviceservice;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.repository.ServiceRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {


    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;


    @Autowired
    public ServiceServiceImpl(UserRepository userRepository, ServiceRepository serviceRepository, ServiceMapper serviceMapper, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;

    }

    /*
     * Get all available services
     * */
    public List<ServiceDTO> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOs = services.stream()
                .map(serviceMapper::convertToServiceDTO)
                .collect(Collectors.toList());
        return serviceDTOs;
    }


    @Override
    public Service getServiceById(Integer serviceId) {
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        return service;
    }


    /*
     * Update price of service
     * */
    @Override
    public ServiceDTO updateService(Integer serviceId, ServiceDTO serviceFromRequest) throws AppointmentServiceNotFoundException {

        // convert dto -> entity
        Service convertedService = serviceMapper.convertToService(serviceFromRequest);

        // check service existed from db
        Service serviceFromDb = serviceRepository.findById(serviceId).orElse(null);
        if (serviceFromDb == null) {
            throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        }
        ServiceDTO result = serviceMapper.convertToServiceDTO(serviceRepository.save(convertedService));
        return result;
    }


    public UserDTO updateUserInfo(int userId, boolean enabled) {
        // Lấy thông tin người dùng từ database
        User userFromDb = userRepository.findById(userId).orElse(null);
        if (userFromDb == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        // Cập nhật thông tin người dùng
        userFromDb.setEnabled(enabled);


        // Lưu người dùng đã cập nhật
        User updatedUser = userRepository.save(userFromDb);

        // Chuyển đổi entity sang DTO và trả về
        return serviceMapper.convertToUserDTO(updatedUser);
    }


}
