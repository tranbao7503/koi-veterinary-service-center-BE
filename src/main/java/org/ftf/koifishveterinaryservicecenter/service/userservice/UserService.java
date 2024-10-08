package org.ftf.koifishveterinaryservicecenter.service.userservice;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> getAllVeterinarians();

    User getUserProfile(Integer userId);

    User updateAddress(Integer userId, Address convertedAddress);

    User updateUserProfile(Integer userId, User convertedCustomer);

    List<User> getAllCustomers();

    void signUp(String username, String password, String first_Name, String last_Name);

    User getVeterinarianById(Integer veterinarianId);

    User updateUserAvatar(Integer userId, MultipartFile image) throws IOException;

    UserDTO createStaff(String userName, String passWord, String firstName, String lastName);
}
