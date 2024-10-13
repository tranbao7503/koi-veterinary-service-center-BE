package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "Image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Integer imageId;

    @Column(name = "source_path", length = 255, nullable = false)
    private String sourcePath;

    @ColumnDefault("b'1'")
    @Column(name = "enable", nullable = false)
    private boolean enabled = true;

    // Bidirectional, identifying relationship
    // Owning side: Image
    // Inverse side: Fish
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fish_id", nullable = false, referencedColumnName = "fish_id")
    private Fish fish;

}
