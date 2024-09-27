package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = AddressMapper.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "avatar", target = "avatar"),
            @Mapping(source = "address", target = "address")
    })
    UserDto convertEntityToDto(User user);

    /*
    * Convert User entity to DTO without Address field
    * */
    @Mapping(target = "address", ignore = true)
    UserDto convertEntityToDtoIgnoreAddress(User user);






}
