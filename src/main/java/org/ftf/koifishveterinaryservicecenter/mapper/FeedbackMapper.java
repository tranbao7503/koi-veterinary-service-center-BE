package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FeedbackMapper.class)
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    /*
    * Map all field of FeedbackDto
    * */
    FeedbackDto convertToFeedbackDto(Feedback feedback);
}
