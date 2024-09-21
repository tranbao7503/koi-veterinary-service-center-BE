package org.ftf.koifishveterinaryservicecenter.model.feedback;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter

@Embeddable
public class FeedbackId implements Serializable {
    private static final long serialVersionUID = -1306138313442962685L;

    @Column(name = "feedback_id", nullable = false)
    private Integer feedbackId;

    @Column(name = "veterinarian_id", nullable = false)
    private Integer veterinarianId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackId that = (FeedbackId) o;
        return Objects.equals(feedbackId, that.feedbackId) &&
                Objects.equals(veterinarianId, that.veterinarianId) &&
                Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackId, veterinarianId, customerId);
    }
}
