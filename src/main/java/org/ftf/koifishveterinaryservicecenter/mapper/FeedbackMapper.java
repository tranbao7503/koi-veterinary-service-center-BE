package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.repository.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public FeedbackMapper(FeedbackRepository feedbackRepository) {
        this.modelMapper = new ModelMapper();
    }

    // Method to convert Feedback entity to Feedback DTO
    public FeedbackDto convertToDto(Feedback feedback) {
        return modelMapper.map(feedback, FeedbackDto.class);
    }

    // Method to convert Feedback DTO to Feedback entity
    public Feedback convertToEntity(FeedbackDto feedbackDto) {
        return modelMapper.map(feedbackDto, Feedback.class);
    }

    // Method to convert a list of Feedback entities to a list of Feedback DTOs
    public List<FeedbackDto> convertToDtoList(List<Feedback> feedbackList) {
        return feedbackList.stream()
                .map(this::convertToDto) // Convert each entity to DTO
                .collect(Collectors.toList()); // Collect results into a list
    }

    // Method to convert a list of Feedback DTOs to a list of Feedback entities
    public List<Feedback> convertToEntityList(List<FeedbackDto> feedbackDtoList) {
        return feedbackDtoList.stream()
                .map(this::convertToEntity) // Convert each DTO to entity
                .collect(Collectors.toList()); // Collect results into a list
    }
}
