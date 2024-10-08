package org.ftf.koifishveterinaryservicecenter.users;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testGetRoleSuccess() {

        Role role = roleRepository.findByRoleKey("CUS");
        Assertions.assertThat(role.getUsers()).isNotNull();

        System.out.println(role.getUsers());
    }

}
