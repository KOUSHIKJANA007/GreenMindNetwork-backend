package com.GreenMindNetwork.service;

import com.GreenMindNetwork.entities.Message;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.payloads.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto getMessageById(Integer messageId);
    List<MessageDto> getAllMessage();
    List<Message> getMessageByUser(Integer userId);
    void deleteChatByUserId(Integer userId);
}
