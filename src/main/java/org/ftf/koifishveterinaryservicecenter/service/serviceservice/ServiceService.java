package org.ftf.koifishveterinaryservicecenter.service.serviceservice;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;

import java.util.List;

public interface ServiceService {
    ServiceDTO updateService(Integer serviceId, ServiceDTO serviceDTO);
    Service getServiceById(Integer serviceId);
    List<ServiceDTO> getAllServices();

    UserDTO updateUserInfo(int userId, boolean enabled);
}
