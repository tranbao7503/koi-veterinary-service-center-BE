package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

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
    @OneToOne(fetch = FetchType.LAZY/*, optional = true*/)
    @JoinColumn(name = "address_id"/*, unique = false, nullable = true*/)
    private Address address;


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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Set<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Set<Appointment> getAllAssignedAppointmentOfVeterinarian() {
        return allAssignedAppointmentOfVeterinarian;
    }

    public void setAllAssignedAppointmentOfVeterinarian(Set<Appointment> allAssignedAppointmentOfVeterinarian) {
        this.allAssignedAppointmentOfVeterinarian = allAssignedAppointmentOfVeterinarian;
    }

    public Set<Appointment> getAllBookedAppointmentOfCustomer() {
        return allBookedAppointmentOfCustomer;
    }

    public void setAllBookedAppointmentOfCustomer(Set<Appointment> allBookedAppointmentOfCustomer) {
        this.allBookedAppointmentOfCustomer = allBookedAppointmentOfCustomer;
    }

    public Set<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(Set<Fish> fishes) {
        this.fishes = fishes;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
