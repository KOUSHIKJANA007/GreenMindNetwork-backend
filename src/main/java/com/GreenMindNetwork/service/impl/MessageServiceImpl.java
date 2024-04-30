package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.entities.Message;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.MessageDto;
import com.GreenMindNetwork.repositories.MessageRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public MessageDto getMessageById(Integer messageId) {
        Message message = this.messageRepo.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));
        return this.modelMapper.map(message,MessageDto.class);
    }

    @Override
    public List<MessageDto> getAllMessage() {
        List<Message> allMessage = this.messageRepo.findAll();
        return allMessage.stream().map(m->this.modelMapper.map(m,MessageDto.class)).toList();
    }

    @Override
    public List<Message> getMessageByUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return this.messageRepo.findByUser(user);
    }
}
