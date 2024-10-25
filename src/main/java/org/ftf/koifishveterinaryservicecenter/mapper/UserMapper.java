package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "avatar", target = "avatar"),
            @Mapping(source = "currentAddress", target = "address"),
            @Mapping(source = "enabled", target = "enable")
    })
    UserDTO convertEntityToDto(User user);

    /*
     * Convert User entity to DTO without Address field
     * */
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDTO convertEntityToDtoIgnoreAddress(User user);


    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "avatar", target = "avatar"),
            @Mapping(source = "address", target = "currentAddress")
    })
    User convertDtoToEntity(UserDTO userDto);


    @Mappings({
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "phoneNumber", ignore = true),
            @Mapping(source = "avatar", target = "avatar"),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "enable", ignore = true)
    })
    UserDTO convertToVeterinarianDto(User user);


}
