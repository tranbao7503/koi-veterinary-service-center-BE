package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FeedbackMapper.class, componentModel = "spring")
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    /*
    * Map all field of FeedbackDto
    * */
    @Mapping(target = "appointment", ignore = true)
    FeedbackDto convertToFeedbackDto(Feedback feedback);

    /*
    * For Feedback DTO to view feedback details
    * */
    @Mapping(source = "appointment.service.serviceName", target = "appointment.serviceName")
    FeedbackDto feedbackToFeedbackDto(Feedback feedback);

}
