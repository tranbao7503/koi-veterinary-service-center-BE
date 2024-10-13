package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.ImageDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mappings({
            @Mapping(source = "imageId", target = "imageId"),
            @Mapping(source = "sourcePath", target = "sourcePath"),
            @Mapping(source = "fish.fishId", target = "fishId"),  // Ánh xạ từ fishId của fish
            @Mapping(source = "enabled", target = "enabled")  // Ánh xạ từ enabled
    })
    ImageDTO convertEntityToDto(Image image);

    @Mappings({
            @Mapping(source = "imageId", target = "imageId"),
            @Mapping(source = "sourcePath", target = "sourcePath"),
            @Mapping(source = "fishId", target = "fish.fishId"),  // Ánh xạ từ fishId vào fish entity
            @Mapping(source = "enabled", target = "enabled")  // Ánh xạ từ enabled
    })
    Image convertDtoToEntity(ImageDTO imageDTO);
}