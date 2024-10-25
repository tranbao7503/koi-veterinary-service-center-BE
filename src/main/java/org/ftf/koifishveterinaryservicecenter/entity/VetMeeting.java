package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "vet_meeting")
@Getter
@Setter
public class VetMeeting {
    @Id
    @Column(name = "vet_id", nullable = false)
    private Integer vetId;

    @OneToOne // Thiết lập mối quan hệ nhiều-một
    @JoinColumn(name = "vet_id", nullable = false, insertable = false, updatable = false) // Ràng buộc với User
    private User vet; // Trường này tham chiếu đến User

    @Column(name = "link_meet", nullable = false)
    private String linkMeet;
}
