package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUsersByUserId(int userId);

    @Query("SELECT u FROM User u WHERE u.userId = :veterinarianId AND u.role.roleKey = 'VET'")
    User findVeterinarianById(@Param("veterinarianId") Integer veterinarianId);

    @Query("SELECT u FROM User u WHERE u.userId = :customerId AND u.role.roleKey = 'CUS'")
    User findCustomerById(@Param("customerId") Integer customerId);


    boolean existsUserByPhoneNumber(String phoneNumber);

    List<User> findAllByRole(Role role);

    List<User> findAllByRoleRoleId(int roleId);


    Optional<User> findByUsername(String username);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleKey = 'VET' AND u.enabled = true")
    long countEnabledVets();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleKey = 'STA' AND u.enabled = true")
    long countEnabledStaff();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleKey = 'CUS' AND u.enabled = true")
    long countEnabledCustomers();


    @Query("SELECT u FROM User u JOIN VeterinarianSlots vs ON u.userId = vs.veterinarianSlotId.veterinarianId WHERE vs.status = 'BOOKED' AND vs.veterinarianSlotId.slotId = :slotId")
    List<User> findBookedVeterinarian(Integer slotId);

}

