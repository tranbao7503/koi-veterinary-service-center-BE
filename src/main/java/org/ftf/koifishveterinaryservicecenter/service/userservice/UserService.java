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

    void signUp(String username, String password, String email, String first_Name, String last_Name);

    User getVeterinarianById(Integer veterinarianId);

    User getCustomerById(Integer customerId);

    User updateUserAvatar(Integer userId, MultipartFile image) throws IOException;

    List<Address> getAllAddresses(Integer customerId);

    Address getAddressById(Integer addressId);

    Address updateAddressDetails(Integer addressId, Address newAddress);

    Address setCurrentAddress(Integer customerId, Integer addressId);

    Address addAddress(Integer customerId, Address address);

    Address disableAddress(Integer addressId);


    UserDTO createStaff(String userName, String passWord, String firstName, String lastName);
}
