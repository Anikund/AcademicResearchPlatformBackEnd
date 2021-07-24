package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Message;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.MessageJpaRepository;
import com.academicresearchplatformbackend.service.MessageService;
import com.academicresearchplatformbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageJpaRepository messageJpaRepository;
    private UserService userService;
    @Autowired
    public void setMessageJpaRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean sendMessage(Long rId, Long sId, String content) {
        Message message = new Message();
        Optional<User> r = userService.findById(rId);
        if (!r.isPresent()) {
            return false;
        }
        Optional<User> s = userService.findById(sId);
        if (!s.isPresent()) {
            return false;
        }
        message.setRead(false);
        message.setReceiver(r.get());
        message.setTransmitter(s.get());
        message.setContent(content);
        messageJpaRepository.save(message);

        return true;
    }

    @Override
    public boolean readMessage(Long id, Long userId) {
        Optional<Message> opMessage = messageJpaRepository.findById(id);

        if (opMessage.isPresent()) {
            Message message = opMessage.get();
            if (message.getReceiver().getId() == userId) {
                message.setRead(true);
                messageJpaRepository.save(message);
                return true;
            }

        }
        return false;
    }

    @Override
    public Message getById(Long id) {
        Optional<Message> opMessage = messageJpaRepository.findById(id);
        if (opMessage.isPresent()) {
            return opMessage.get();
        } else {
            return null;

        }
    }

    @Override
    public Page<Message> getByReceiver(User user, int page, int size) {
        return messageJpaRepository.findAllByReceiver(user, PageRequest.of(page, size, Sort.by("sentTime").descending()));
    }


    @Override
    public Page<Message> getByTransmitter(User user, int page, int size) {
        return messageJpaRepository.findAllByTransmitter(user, PageRequest.of(page, size, Sort.by("sentTime").descending()));
    }

    @Override
    public Page<Message> getByTimeBetween(Timestamp fore, Timestamp end, int page, int size) {
        return messageJpaRepository.findAllBySentTimeBetween(fore, end, PageRequest.of(page, size, Sort.by("sentTime").descending()));
    }

    @Override
    public Page<Message> getAllMessages(int page, int size) {
        return messageJpaRepository.findAll(PageRequest.of(page, size, Sort.by("sentTime").descending()));
    }


}
