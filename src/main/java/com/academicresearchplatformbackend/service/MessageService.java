package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Message;
import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

public interface MessageService {
     boolean sendMessage(Long rId, Long sId, String content);

     boolean readMessage(Long id, Long userId);

     Message getById(Long id);

     Page<Message> getByReceiver(User user, int page, int size);

     Page<Message> getByTransmitter(User user, int page, int size);

     Page<Message> getByTimeBetween(Timestamp fore, Timestamp end, int page, int size);

     Page<Message> getAllMessages(int page, int size);
}

