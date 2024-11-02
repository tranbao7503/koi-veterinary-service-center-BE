package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {


    // This query is only used for reading permission in home page
    @Query("SELECT f FROM Feedback f ORDER BY f.rating LIMIT 10")
    List<Feedback> findFeedbackByRating();

    @Query("select s from Feedback s where s.veterinarian.userId = :veterinarianId")
    List<Feedback> findByVeterianrianId(@Param("veterinarianId") Integer veterianrianId);

    // Đếm số lượng feedback trong ngày
    @Query("SELECT COUNT(f) FROM Feedback f WHERE DATE(f.datetime) = CURRENT_DATE")
    long countFeedbackToday();

    // Đếm số lượng feedback trong tháng
    @Query("SELECT COUNT(f) FROM Feedback f WHERE MONTH(f.datetime) = :month AND YEAR(f.datetime) = :year")
    long countFeedbackByMonth(@Param("month") int month, @Param("year") int year);

    // Đếm số lượng feedback trong quý
    @Query("SELECT COUNT(f) FROM Feedback f WHERE QUARTER(f.datetime) = :quarter AND YEAR(f.datetime) = :year")
    long countFeedbackByQuarter(@Param("quarter") int quarter, @Param("year") int year);

    @Query("SELECT f.veterinarian.id, AVG(f.rating) FROM Feedback f GROUP BY f.veterinarian.id")
    List<Object[]> averageRatingPerVet();

    @Query("SELECT AVG(f.rating) FROM Feedback f JOIN f.appointment a WHERE a.service.serviceId = :serviceId")
    BigDecimal findAverageRatingByServiceId(@Param("serviceId") Integer serviceId);

    @Query("SELECT f FROM Feedback f JOIN f.appointment a WHERE a.service.serviceId = :serviceId AND f.rating > 4")
    List<Feedback> findFeedbacksAboveRatingByServiceId(@Param("serviceId") Integer serviceId);




}