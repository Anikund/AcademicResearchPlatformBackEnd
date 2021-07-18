package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;



    @Test
    public void sendMessage() {
        messageService.sendMessage(1L, 1L, "test:1->1)");
        messageService.sendMessage(1L, 3L, "test:1->3)");
        messageService.sendMessage(3L, 1L, "test:3->1)");
    }


    @Test
    public void readMessage() {
        messageService.readMessage(1L, 1L);
        assertTrue(messageService.getById(1L).isRead());

    }

    @Test
    public void getById() {
    }

    @Test
    public void getByReceiver() {
    }

    @Test
    public void getByTransmitter() {
    }

    @Test
    public void getByTimeBetween() {
    }

    @Test
    public void getAllMessages() {
    }
}
