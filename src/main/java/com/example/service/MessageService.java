package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.repository.MessageRepository;
import com.example.entity.Message;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    // save and return message
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    // find all messages
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    // find message by id
    public Message findByMessageId(int messageId) {
        return messageRepository.findByMessageId(messageId);
    }
}
