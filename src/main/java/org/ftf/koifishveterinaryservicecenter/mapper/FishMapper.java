package org.ftf.koifishveterinaryservicecenter.mapper;


import org.ftf.koifishveterinaryservicecenter.dto.FishDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = FishMapper.class)
public interface FishMapper {

    FishMapper INSTANCE = Mappers.getMapper(FishMapper.class);

    @Mappings({
            @Mapping(source = "fishId", target = "fishId"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "age", target = "age"),
            @Mapping(source = "species", target = "species"),
            @Mapping(source = "size", target = "size"),
            @Mapping(source = "weight", target = "weight"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "origin", target = "origin"),
            @Mapping(source = "customer.userId", target = "customerId")  // Ánh xạ từ userId của customer
    })
    FishDTO convertEntityToDto(Fish fish);
}
