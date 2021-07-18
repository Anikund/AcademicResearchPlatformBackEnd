package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
}
