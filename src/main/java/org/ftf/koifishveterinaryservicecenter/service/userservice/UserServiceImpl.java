package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenicationException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.AddressRepository;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.ftf.koifishveterinaryservicecenter.service.fileservice.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , AddressRepository addressRepository
            , RoleRepository roleRepository
            , PasswordEncoder passwordEncoder
            , FileUploadService fileUploadService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public User getUserProfile(Integer userId) {
        return userRepository.findUsersByUserId(userId);
    }

    @Override
    public List<User> getAllVeterinarians() {
        Role role = roleRepository.findByRoleKey("VET");
        List<User> veterinarians = new ArrayList<>(role.getUsers());
        return veterinarians;
    }

    @Override
    @Transactional
    public User updateAddress(Integer userId, Address convertedAddress) {
        User userFromDb = userRepository.findUsersByUserId(userId);

        if (userFromDb == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }

        // set addressId for Address input
        Integer addressId = userFromDb.getAddress().getAddressId();
        convertedAddress.setAddressId(addressId);

        // update Address property for User instance
        Address updatedAddress = addressRepository.save(convertedAddress);
        userFromDb.setAddress(updatedAddress);
        return userFromDb;
    }

    @Override
    @Transactional
    public User updateUserProfile(Integer userId, User convertedCustomer) {
        User userFromDb = userRepository.findUsersByUserId(userId);

        if (userFromDb == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }

        // set addressId for User input
        Integer customerId = userFromDb.getUserId();
        convertedCustomer.setUserId(userId);

        // fill in empty fields


        // check firstname
        if (convertedCustomer.getFirstName() != null) {
            userFromDb.setFirstName(convertedCustomer.getFirstName());
        }

        // check lastname
        if (convertedCustomer.getLastName() != null) {
            userFromDb.setLastName(convertedCustomer.getLastName());
        }

        // check phone number
        String phoneNumber = convertedCustomer.getPhoneNumber();
        if (!phoneNumber.equals(userFromDb.getPhoneNumber()) && !userRepository.existsUserByPhoneNumber(phoneNumber)) {
            userFromDb.setPhoneNumber(phoneNumber);
        }

        userFromDb = userRepository.save(userFromDb);

        // update user's profile for User instance
        return userFromDb;
    }

    @Override
    public List<User> getAllCustomers() {
        Role role = roleRepository.findByRoleKey("CUS");
        List<User> customers = new ArrayList<>(role.getUsers());
        return customers;
    }


    @Override
    public void signUp(String username, String password, String first_Name, String last_Name) {

        if (username == null || username.isBlank()) {
            throw new AuthenicationException("Username can not be empty");
        }
        if (username.contains(" ")) {
            throw new AuthenicationException("Username can not contain white space");
        }
        if (password == null || password.isBlank()) {
            throw new AuthenicationException("Password can not be empty");
        }
        if (password.length() < 8) {
            throw new AuthenicationException("Password can not < 8 characters");
        }
        String passwordPattern = "^(?=.*[@#$%^&+=!{}]).{8,}$";
        if (!password.matches(passwordPattern)) {
            throw new AuthenicationException("Password must contain at least one special character and be at least 8 characters long");
        }

        if (first_Name == null || first_Name.isBlank()) {
            throw new AuthenicationException("first_Name can not be empty");
        }
        if (last_Name == null || last_Name.isBlank()) {
            throw new AuthenicationException("last_Name can not be empty");
        }
        if (userRepository.findUserByUsername(username) != null) {
            throw new AuthenicationException("Username is existed");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByRoleKey("CUS");
        user.setRole(role);
        user.setFirstName(first_Name);
        user.setLastName(last_Name);
        userRepository.save(user);
    }


    @Override
    public User getVeterinarianById(Integer veterinarianId) {
        User veterinarian = userRepository.findVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new UserNotFoundException("Veterinarian not found with Id: " + veterinarianId);
        }
        return veterinarian;
    }


    @Override
    public User updateUserAvatar(Integer userId, MultipartFile image) throws IOException {
        User user = userRepository.findUsersByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }
        String path = fileUploadService.uploadFile(image);
        user.setAvatar(path);
        userRepository.save(user);
        return user;
    }


}
