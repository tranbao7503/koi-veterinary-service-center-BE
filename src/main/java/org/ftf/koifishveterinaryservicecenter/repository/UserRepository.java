package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByRoleRoleId(Integer roleID);

    // viet quert
    List<User> findAllCustomer(String role);
    List<User> findAllStaff(String role);
    List<User> findAllVeterianrian(String role);
}