package org.ftf.koifishveterinaryservicecenter.serviceservice;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.ftf.koifishveterinaryservicecenter.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest // enables JPA testing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Replace.NONE means telling Spring Boot use the existing database for testing
@Rollback
public class ServiceRepositoryTests {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void testGetAllServicesSuccess() {
        List<Service> serviceList = serviceRepository.findAll();

        Assertions.assertThat(serviceList).isNotEmpty();
        serviceList.forEach(System.out::println);
    }


    @Test
    public void testGetServiceByIdSuccess() {
        Integer serviceId = 1;
        Optional<Service> service = serviceRepository.findById(serviceId);
        Assertions.assertThat(service).isPresent();
        System.out.println(service.toString());
    }

}
