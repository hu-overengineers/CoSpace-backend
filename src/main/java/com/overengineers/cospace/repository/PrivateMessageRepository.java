package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findByReceiverUsernameOrSenderUsername(String receiver_username, String sender_username);
}
