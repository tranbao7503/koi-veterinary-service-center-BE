package org.ftf.koifishveterinaryservicecenter.veterinarianslots;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlotId;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.ftf.koifishveterinaryservicecenter.enums.SlotStatus;
import org.ftf.koifishveterinaryservicecenter.exception.TimeSlotNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.TimeSlotRepository;
import org.ftf.koifishveterinaryservicecenter.repository.VeterinarianSlotsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class VeterinarianSlotsRepositoryTests {
    @Autowired
    private VeterinarianSlotsRepository veterinarianSlotsRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Test
    public void testGetVeterinarianSlotsByCompositeId() {
        Integer veterinarianSlotId = 13;
        Integer slotId = 177;
        VeterinarianSlots veterinarianSlot = veterinarianSlotsRepository.getVeterinarianSlotsById(veterinarianSlotId, slotId);
        Assertions.assertThat(veterinarianSlot).isNotNull();
    }




    @Test
    public void testGetAllAvailableSlotsSuccess(){
        LocalDateTime threeHoursFromNow = LocalDateTime.now().plusHours(3);
        LocalDateTime threeMonthsFromNow = LocalDateTime.now().plusMonths(3);

        List<TimeSlot> availableTimeSlot = timeSlotRepository.getAvailableTimeSlot();
        availableTimeSlot.stream().filter(timeSlot -> timeSlot.getDateTimeBasedOnSlot().isAfter(threeHoursFromNow) && timeSlot.getDateTimeBasedOnSlot().isBefore(threeMonthsFromNow) && hasAvailableVeterinarian(timeSlot.getSlotId())).toList();

        System.out.println(availableTimeSlot.size());
    }

    private boolean hasAvailableVeterinarian(Integer slotId){
        return getVeterinarianSlotsBySlotId(slotId).size() > 0;
    }

    public List<VeterinarianSlots> getVeterinarianSlotsBySlotId(Integer slotId ) {
        List<VeterinarianSlots> veterinarianSlots = veterinarianSlotsRepository.getAvailableSlotsBySlotId(SlotStatus.AVAILABLE, slotId);
        return veterinarianSlots;
    }

    @Test
    public void testUpdateVeterinarianSlotsSuccess(){
        Integer veterinarianId = 17;
        Integer slotId = 260;

        VeterinarianSlotId veterinarianSlotId = new VeterinarianSlotId();
        veterinarianSlotId.setSlotId(slotId);
        veterinarianSlotId.setVeterinarianId(veterinarianId);

        VeterinarianSlots veterinarianSlots = veterinarianSlotsRepository.findById(veterinarianSlotId).get();

        veterinarianSlots.setStatus(SlotStatus.UNAVAILABLE);

        veterinarianSlotsRepository.save(veterinarianSlots);
    }
}
