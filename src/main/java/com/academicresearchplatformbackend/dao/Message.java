package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
//import java.sql.Date;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User transmitter;
    @OneToOne
    private User receiver;

    private Timestamp sentTime;

    private boolean isRead;

    private String content;

    @PrePersist
    public void setSentTime() {
        Date date = new Date();
        this.sentTime = new Timestamp(date.getTime());
    }
}
