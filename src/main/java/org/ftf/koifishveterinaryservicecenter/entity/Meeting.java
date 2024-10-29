package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "meeting")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "link_meet", nullable = false)
    private String linkMeet;

}
