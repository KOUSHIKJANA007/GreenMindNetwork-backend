package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.entities.Message;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.MessageDto;
import com.GreenMindNetwork.repositories.MessageRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MessageService messageService;
    @MessageMapping("/message")
    public ResponseEntity<MessageDto> sendMessageTo(@RequestBody MessageDto messageDto){
        User user = this.userRepo.findById(messageDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", messageDto.getUserId()));
        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverName(),"/private",messageDto);
        Message message=new Message();
        message.setContent(messageDto.getContent());
        message.setUser(user);
        this.messageRepo.save(message);
        return ResponseEntity.ok(messageDto);
    }
    @GetMapping("/message/all")
    public ResponseEntity<List<MessageDto>> getAllMessage(){
        List<MessageDto> allMessage = this.messageService.getAllMessage();
        return ResponseEntity.ok(allMessage);
    }
    @GetMapping("/message/{messageId}")
    public ResponseEntity<MessageDto> getMessage(@PathVariable Integer messageId){
        return ResponseEntity.ok(this.messageService.getMessageById(messageId));
    }
    @GetMapping("/message/user/{userId}")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Integer userId){
        return ResponseEntity.ok(this.messageService.getMessageByUser(userId));
    }
    @DeleteMapping("/message/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteChatByUser(@PathVariable Integer userId){
        this.messageService.deleteChatByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("Clear all chat data",true));
    }
}
