package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VetMeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByVetId(Integer vetId);
}
