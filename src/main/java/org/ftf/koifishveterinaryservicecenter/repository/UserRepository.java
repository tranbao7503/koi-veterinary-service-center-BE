package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findUsersByUserId(int userId);

  @Query("SELECT u FROM User u WHERE u.userId = :veterinarianId AND u.role.roleKey = 'VET'")
  User findVeterinarianById(@Param("veterinarianId") Integer veterinarianId);

  boolean existsUserByPhoneNumber(String phoneNumber);

  List<User> findAllByRole (Role role);

  List<User>findAllByRoleRoleId(int roleId);

  User findUserByUsername(String username);

  }
