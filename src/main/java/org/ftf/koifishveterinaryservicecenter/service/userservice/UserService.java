package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDto;

public interface UserService {
    UserDto getUserProfile(Integer userId);
}
