package org.ftf.koifishveterinaryservicecenter.service.userservice;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.util.List;

public interface UserService {
    User getUserProfile(Integer userId);
    User updateAddress(Integer userId, Address convertedAddress);
    User updateUserProfile(Integer userId, User convertedCustomer);
    List<User> getAllCustomers();
}
