package org.ftf.koifishveterinaryservicecenter.movingsurcharge;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;
import org.ftf.koifishveterinaryservicecenter.repository.MovingSurchargeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class MovingSurchargeRepositoryTests {

    @Autowired
    private MovingSurchargeRepository movingSurchargeRepository;

    @Test
    public void testGetMovingPriceByAddressIdSuccess(){
        Integer addressId = 1;
        MovingSurcharge movingPrice = movingSurchargeRepository.getMovingSurchargeByAddressId(addressId);
        Assertions.assertThat(movingPrice).isNotNull();
        System.out.println(movingPrice.toString());
    }
}
