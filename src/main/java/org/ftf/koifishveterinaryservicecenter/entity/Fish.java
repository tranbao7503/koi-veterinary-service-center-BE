package org.ftf.koifishveterinaryservicecenter.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.enums.Gender;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fish")
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fish_id", nullable = false)
    private Integer fishId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "species", nullable = false, length = 50)
    private String species;

    @Column(name = "size", nullable = false, precision = 10, scale = 2)
    private BigDecimal size;

    @Column(name = "weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "color", nullable = false, length = 45)
    private String color;

    @Column(name = "origin", nullable = false, length = 45)
    private String origin;

    @ColumnDefault("b'1'")
    @Column(name = "enable", nullable = false)
    private boolean enabled = true;


    // Bidirectional, identifying relationship
    // Owning side: Fish
    // Inverse side: User(customer)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)     // A Fish must belong to a User
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "user_id")
    private User customer;

    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Fish
    @OneToMany(mappedBy = "fish", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new LinkedHashSet<>();

    // Bidirectional, identifying relationship
    // Owning side: Image
    // Inverse side: Fish
    @OneToMany(mappedBy = "fish", fetch = FetchType.LAZY)
    @JsonManagedReference // Thêm annotation này
    private Set<Image> images = new LinkedHashSet<>();


}

