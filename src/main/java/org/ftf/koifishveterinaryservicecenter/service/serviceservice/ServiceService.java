package org.ftf.koifishveterinaryservicecenter.service.serviceservice;

import org.ftf.koifishveterinaryservicecenter.entity.Service;

import java.util.List;

public interface ServiceService {
    Service updateService(Integer serviceId, Service service);
    Service getServiceById(Integer serviceId);

    List<Service> getAllServices();
}
