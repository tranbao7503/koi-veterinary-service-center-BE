package org.ftf.koifishveterinaryservicecenter.service.serviceservice;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;

import java.util.List;

public interface ServiceService {
    ServiceDTO updateService(Integer serviceId, ServiceDTO serviceDTO);
    ServiceDTO getServiceById(Integer serviceId);
    List<ServiceDTO> getAllServices();
}
