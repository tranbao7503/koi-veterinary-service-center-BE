package org.ftf.koifishveterinaryservicecenter.users;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class AddressRepositoryTests {
    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void testGetAddressByIdSuccess() {
        int addressId = 1;
        Address address = addressRepository.findById(addressId).get();
        Assertions.assertThat(address).isNotNull();
        System.out.println(address);
    }
}
