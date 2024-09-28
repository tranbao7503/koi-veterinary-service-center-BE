package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
}
