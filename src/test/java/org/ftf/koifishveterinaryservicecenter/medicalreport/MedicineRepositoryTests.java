package org.ftf.koifishveterinaryservicecenter.medicalreport;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.repository.MedicineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class MedicineRepositoryTests {

    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    public void testGetAllMedicineSuccess(){
        List<Medicine> medicines = medicineRepository.findAll();
        Assertions.assertThat(medicines).isNotEmpty();
        medicines.forEach(System.out::println);
    }

}
