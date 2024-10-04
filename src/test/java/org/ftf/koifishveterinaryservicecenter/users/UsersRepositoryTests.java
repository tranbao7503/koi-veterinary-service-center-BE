package org.ftf.koifishveterinaryservicecenter.users;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class UsersRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUserSuccess() {
        int userId = 1;
        User user = userRepository.findUsersByUserId(userId);

        Assertions.assertThat(user.getAddress()).isNotNull();
        System.out.println(user);
    }

    @Test
    public void testUpdateUserSuccess() {
        String firstName = "John_New";

        int userId = 1;
        User user = userRepository.findUsersByUserId(userId);
        user.setFirstName(firstName);
        userRepository.save(user);
        System.out.println(user.getFirstName());
    }

    @Test
    public void testCreateMedicalReportForAppointmentSuccess() {
        int veterinarianId = 3;
        User veterinarian = userRepository.findUsersByUserId(veterinarianId);

        Assertions.assertThat(veterinarian).isNotNull();
        System.out.println(veterinarian.getUserId());
    }


}
