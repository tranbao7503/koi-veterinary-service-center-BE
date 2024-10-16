package org.ftf.koifishveterinaryservicecenter.veterinarianslots;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.ftf.koifishveterinaryservicecenter.repository.VeterinarianSlotsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class VeterinarianSlotsRepositoryTests {
    @Autowired
    private VeterinarianSlotsRepository veterinarianSlotsRepository;

    @Test
    public void testGetVeterinarianSlotsByCompositeId() {
        Integer veterinarianSlotId = 11;
        Integer slotId = 177;
        VeterinarianSlots veterinarianSlot = veterinarianSlotsRepository.getVeterinarianSlotsById(veterinarianSlotId,slotId);
        Assertions.assertThat(veterinarianSlot).isNotNull();
        System.out.println(veterinarianSlot.toString());
    }
}
