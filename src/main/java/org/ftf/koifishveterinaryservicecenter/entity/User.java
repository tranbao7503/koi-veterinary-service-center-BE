package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = true, length = 50, unique = true)
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // optional = false  <-> user must have a role
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "role_id")
    private Role role;


    // Uni-directional, non-identifying relationship
    // Owning side: User
    // Inverse side: Address
    @OneToMany(mappedBy = "customer"/*, optional = true*/)
    //@JoinColumn(name = "address_id"/*, unique = false, nullable = true*/)
    private Set<Address> addresses = new LinkedHashSet<>();


    // Bidirectional, identifying relationship
    // Owning side: Fish
    // Inverse side: User(customer)
    @OneToMany(mappedBy = "customer"/*, orphanRemoval = true*/) // Shouldn't allow to remove data
    // orphanRemoval: true -->  remove User then all related Fishes will be removed
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

    // Bidirectional, identifying  relationship
    // Owning side: VeterinarianSlots
    // Inverse side: User(Veterinarian)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "veterinarian_slots",
            joinColumns = @JoinColumn(name = "veterinarian_id"),
            inverseJoinColumns = @JoinColumn(name = "slot_id")
    )
    private Set<TimeSlot> timeSlots = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_address_id", nullable = true)
    private Address currentAddress;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address=" + addresses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(avatar, user.avatar) && Objects.equals(role, user.role) && Objects.equals(addresses, user.addresses) && Objects.equals(fishes, user.fishes) && Objects.equals(allBookedAppointmentOfCustomer, user.allBookedAppointmentOfCustomer) && Objects.equals(allAssignedAppointmentOfVeterinarian, user.allAssignedAppointmentOfVeterinarian) && Objects.equals(timeSlots, user.timeSlots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, firstName, lastName, phoneNumber, avatar, enabled, role, addresses, fishes, allBookedAppointmentOfCustomer, allAssignedAppointmentOfVeterinarian, timeSlots);
    }
}
