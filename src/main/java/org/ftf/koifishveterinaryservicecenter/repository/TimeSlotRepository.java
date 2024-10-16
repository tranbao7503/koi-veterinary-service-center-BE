package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    @Query("SELECT t FROM TimeSlot t JOIN VeterinarianSlots vs ON t.slotId = vs.veterinarianSlotId.slotId WHERE vs.veterinarian.userId = :veterinarianId AND vs.status = 'BOOKED'")
    List<TimeSlot> findByVeterinarianId(Integer veterinarianId);


    @Query("SELECT t FROM TimeSlot t " +
            "WHERE (t.year > YEAR(NOW())) " +
            "OR (t.year = YEAR(NOW()) " +
            "AND t.month >= MONTH(NOW()))" +
            "ORDER BY t.year, t.month, t.day, t.slotOrder")
    List<TimeSlot> getAvailableTimeSlot();


    @Query("SELECT DISTINCT ts FROM TimeSlot ts JOIN VeterinarianSlots vs ON ts.slotId = vs.timeSlot.slotId " +
            "WHERE vs.status = 'AVAILABLE' AND " +
            "((ts.year > :currentYear OR (ts.year = :currentYear AND ts.month > :currentMonth) OR " +
            "(ts.year = :currentYear AND ts.month = :currentMonth AND ts.day > :currentDay) OR " +
            "(ts.year = :currentYear AND ts.month = :currentMonth AND ts.day = :currentDay AND ts.slotOrder >= :beginSlot)) AND " +
            "(ts.year < :endYear OR (ts.year = :endYear AND ts.month < :endMonth) OR " +
            "(ts.year = :endYear AND ts.month = :endMonth AND ts.day <= :endDay)))")
    List<TimeSlot> findAvailableTimeSlot(@Param("currentYear") Integer currentYear,
                                         @Param("currentMonth") Integer currentMonth,
                                         @Param("currentDay") Integer currentDay,
                                         @Param("beginSlot") Integer beginSlot,
                                         @Param("endYear") Integer endYear,
                                         @Param("endMonth") Integer endMonth,
                                         @Param("endDay") Integer endDay);

    @Query("SELECT DISTINCT ts FROM TimeSlot ts JOIN VeterinarianSlots vs ON ts.slotId = vs.timeSlot.slotId " +
            "WHERE vs.status = 'AVAILABLE' AND " +
            "vs.veterinarian.userId = :veterinarianId AND" +
            "((ts.year > :currentYear OR (ts.year = :currentYear AND ts.month > :currentMonth) OR " +
            "(ts.year = :currentYear AND ts.month = :currentMonth AND ts.day > :currentDay) OR " +
            "(ts.year = :currentYear AND ts.month = :currentMonth AND ts.day = :currentDay AND ts.slotOrder >= :beginSlot)) AND " +
            "(ts.year < :endYear OR (ts.year = :endYear AND ts.month < :endMonth) OR " +
            "(ts.year = :endYear AND ts.month = :endMonth AND ts.day <= :endDay)))")
    List<TimeSlot> findAvailableTimeSlotByVeterinarianId(@Param("veterinarianId") Integer veterinarianId,
                                                         @Param("currentYear") Integer currentYear,
                                                         @Param("currentMonth") Integer currentMonth,
                                                         @Param("currentDay") Integer currentDay,
                                                         @Param("beginSlot") Integer beginSlot,
                                                         @Param("endYear") Integer endYear,
                                                         @Param("endMonth") Integer endMonth,
                                                         @Param("endDay") Integer endDay);
}
