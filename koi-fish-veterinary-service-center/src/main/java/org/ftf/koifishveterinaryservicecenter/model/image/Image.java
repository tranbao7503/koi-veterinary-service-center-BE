package org.ftf.koifishveterinaryservicecenter.model.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.baopen753.koifishveterinaryservice.model.fish.Fish;

@Getter
@Setter
@Entity
@Table(name = "Image")
public class Image {

    @EmbeddedId
    private ImageId id;

    @Column(name = "source_path", length = 255, nullable = false)
    private String sourcePath;

    // Bidirectional, identifying relationship
    // Owning side: Image
    // Inverse side: Fish
    @MapsId("fishId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fish_id", nullable = false, referencedColumnName = "fish_id")
    private Fish fish;

}
