package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "meeting")
@Getter
@Setter
public class Meeting {
    @Id
    @Column(name = "vet_id", nullable = false)
    private Integer vetId;

    @OneToOne(mappedBy = "meeting") // Thiết lập mối quan hệ một-một
    private User vet; // Tham chiếu đến User

    @Column(name = "link_meet", nullable = false)
    private String linkMeet;

}
