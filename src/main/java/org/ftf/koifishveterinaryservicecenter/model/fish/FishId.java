package org.ftf.koifishveterinaryservicecenter.model.fish;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class FishId {

    @Column(name = "fish_id", nullable = false)
    private Integer fishId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FishId fishId1 = (FishId) o;
        return Objects.equals(fishId, fishId1.fishId) && Objects.equals(userId, fishId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fishId, userId);
    }
}
