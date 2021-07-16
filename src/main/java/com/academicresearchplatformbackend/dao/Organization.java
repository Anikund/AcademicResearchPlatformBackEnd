package com.academicresearchplatformbackend.dao;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Organization {
    @Id
    private Long id;
    @NotNull
    private String name;
    private String location;
    private String tel;

}
