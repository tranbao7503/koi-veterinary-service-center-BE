package org.ftf.koifishveterinaryservicecenter.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Integer id;

    @Column(name = "certificate_name", nullable = false)
    private String certificateName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinarian_id", nullable = false, referencedColumnName = "user_id")
    private User veterinarian;
}
