package com.example.demo.mapper;

import com.example.demo.dto.request.MessageCreationRequest;
//import com.example.demo.dto.request.MessageUpdateRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
//    Message toMessage(MessageCreationRequest request);
//    void updateMessage(@MappingTarget Message Message, MessageUpdateRequest request);
//    MessageResponse toMessageResponse(Message message);
}
