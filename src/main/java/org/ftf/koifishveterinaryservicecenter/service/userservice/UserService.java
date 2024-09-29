package org.ftf.koifishveterinaryservicecenter.service.userservice;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllVeterinarians();

    User getUserProfile(Integer userId);

    User updateAddress(Integer userId, Address convertedAddress);

    User updateUserProfile(Integer userId, User convertedCustomer);

    List<User> getAllCustomers();

    List<User> getAllStaffsAndVeterinarians();

    void signUp(String username,String password,String first_Name,String last_Name);

}
