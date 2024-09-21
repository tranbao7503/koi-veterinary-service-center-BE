package org.ftf.koifishveterinaryservicecenter.model.user;

import jakarta.persistence.*;
import org.baopen753.koifishveterinaryservice.model.address.Address;
import org.baopen753.koifishveterinaryservice.model.appointment.Appointment;
import org.baopen753.koifishveterinaryservice.model.fish.Fish;
import org.baopen753.koifishveterinaryservice.model.role.Role;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @EmbeddedId
    private UserId userId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", nullable = true, length = 10)
    private String phoneNumber;

    @Column(name = "avatar", nullable = true, length = 255)
    private String avatar;

    @ColumnDefault("b'1'")
    @Column(name = "enable", nullable = false)
    private boolean enabled = true;


    // Uni-directional, identifying relationship
    // owning side: User
    // inverse side: Role
    @MapsId("roleId")   // map to 'role_id' of above EmbeddedId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // optional = false  <-> user must have a role
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "role_id")
    private Role role;


    // Uni-directional, non-identifying relationship
    // Owning side: User
    // Inverse side: Address
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;


    // Bidirectional, identifying relationship
    // Owning side: Fish
    // Inverse side: User
    @OneToMany(mappedBy = "fish", orphanRemoval = true)    // orphanRemoval: true -->  remove User then all related Fishes will be removed
    private Set<Fish> fishes = new LinkedHashSet<>();

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: User(customer)
    @OneToMany(mappedBy = "customer")
    private Set<Appointment> allBookedAppointmentOfCustomer = new LinkedHashSet<>();

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: User(veterinarian)
    @OneToMany(mappedBy = "veterinarian")
    private Set<Appointment> allAssignedAppointmentOfVeterinarian = new LinkedHashSet<>();



}
