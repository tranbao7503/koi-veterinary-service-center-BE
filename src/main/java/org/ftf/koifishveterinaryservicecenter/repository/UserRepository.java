package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUsersByUserId(int userId);

    boolean existsUserByPhoneNumber(String phoneNumber);

   default List<User> findAllByRole (Role role){
       return findAllByRoleRoleId(role.getValue());
   }
   List<User>findAllByRoleRoleId(int roleId);


    Optional<User> findByUsername(String username);


}


