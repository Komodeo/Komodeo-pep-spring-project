package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // find message by message id
    Message findByMessageId(int messageId);

    // delete message by message id
    Integer deleteByMessageId(int messageId);

    // find all messages posted by a specific user id
    List<Message> findAllByPostedBy(int postedBy);
}
