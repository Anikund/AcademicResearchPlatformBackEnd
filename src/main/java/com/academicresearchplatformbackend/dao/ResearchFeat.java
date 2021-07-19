package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ResearchFeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;//类型
    @ManyToMany
    @JoinTable(name="feat_users",
            joinColumns = @JoinColumn(name="feat_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> users;
    private String journalName;//收录期刊/xxx名字
    private Integer citedCount;//被引用数量
    private String description;//简介
    private String level;//级别
    @ManyToMany
    @JoinTable(name="feat_resources",
            joinColumns = @JoinColumn(name="feat_id"),
            inverseJoinColumns = @JoinColumn(name="resource_id"))
    private List<FileResource> resources= new ArrayList<>();
//
}
