package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.VetMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VetMeetingRepository extends JpaRepository<VetMeeting, Long> {
    Optional<VetMeeting> findByVetId(Integer vetId);
}
