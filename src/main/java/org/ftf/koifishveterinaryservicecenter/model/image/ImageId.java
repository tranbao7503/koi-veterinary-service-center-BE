package org.ftf.koifishveterinaryservicecenter.model.image;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ImageId {

    @Column(name = "image_id", nullable = false)
    private Integer imageId;

    @Column(name = "fish_id", nullable = false)
    private Integer fishId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageId imageId1 = (ImageId) o;
        return Objects.equals(imageId, imageId1.imageId) && Objects.equals(fishId, imageId1.fishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, fishId);
    }
}
