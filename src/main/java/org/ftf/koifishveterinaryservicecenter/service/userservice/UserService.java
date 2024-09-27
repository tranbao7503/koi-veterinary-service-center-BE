package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.util.List;

public interface UserService {
    UserDto getUserProfile(Integer userId);
    List<User> getAllVeterinarians();
}
