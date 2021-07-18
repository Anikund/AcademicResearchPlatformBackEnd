package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
//Resource on the server, such as documentations, movies, etc.
public class FileResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//id
    private String url;//resource url on the server
    private Timestamp createdTime;//
    private String name;//file name

    @PrePersist
    public void setCreatedTime() {
        this.createdTime = new Timestamp(new Date().getTime());
    }
}
