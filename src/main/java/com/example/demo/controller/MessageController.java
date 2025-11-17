package com.example.demo.controller;

import com.example.demo.dto.request.MessageCreationRequest;
import com.example.demo.model.ChatMessage;
import com.example.demo.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MessageController {
    MessageService messageService;

    @MessageMapping("/sendPrivateMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message){
        message.setTimestamp(LocalDateTime.now());
        log.info(message.toString());
        return message;
    }

    @MessageMapping("/leave")
    @SendTo("topic/messages/{username}")
    public ChatMessage leaveMessage(@PathVariable String username){
        ChatMessage newMessage = new ChatMessage();
        newMessage.setContent(username+" left the chat");
        newMessage.setSender(username);
        newMessage.setType("LEAVE");
        newMessage.setTimestamp(LocalDateTime.now());
        log.info(newMessage.toString());
        return newMessage;
    }
    @MessageMapping("/join")
    @SendTo("topic/messages/{username}")
    public ChatMessage joinMessage(@PathVariable String username){
        ChatMessage newMessage = new ChatMessage();
        newMessage.setContent(username+" joined the chat");
        newMessage.setSender(username);
        newMessage.setType("JOIN");
        newMessage.setTimestamp(LocalDateTime.now());
        log.info(newMessage.toString());
        return newMessage;
    }
}
