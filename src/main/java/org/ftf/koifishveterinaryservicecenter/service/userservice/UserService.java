package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    UserDTO updateUserInfo(int userId, boolean enabled);

    List<Address> getAllAddresses(Integer customerId);

    Address getAddressById(Integer addressId);

    Address updateAddressDetails(Integer addressId, Address newAddress);

    Address setCurrentAddress(Integer customerId, Integer addressId);

    Address addAddress(Integer customerId, Address address);

    Address disableAddress(Integer addressId);


    UserDTO createStaff(String userName, String passWord, String firstName, String lastName);

    List<User> getAllStaffs();

    UserDTO updatePassword(String newPassword);

    Map<String, String> getUserAndFishStatistics();


    // Hàm tính số liệu cuộc hẹn
    Map<String, String> getAppointmentStatistics();

    // Hàm tính số liệu thanh toán
    Map<String, String> getPaymentStatistics();

    long getVetSlotsInCurrentWeek(int vetId);

    //them so luong feedback voi so luong sao trung binh cua bac si
    Map<String, Object> getFeedbackStatistics();

    List<User> getBookedVeterinarianBySlotId(Integer slotId);

}
