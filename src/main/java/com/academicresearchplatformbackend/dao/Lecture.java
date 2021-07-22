package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String description;//简介
    private String lecturer;//主讲人信息
    private int fund;//经费
    @ManyToMany
    @JoinTable(name = "lecture_resources",
            joinColumns = @JoinColumn(name = "lecture_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    List<FileResource> resources = new ArrayList<>();//相关文件资源
    private String level;//级别
    private String content;//内容该要
    private boolean isAvailable;//是否可行，比如是否结束之类的
    @ManyToMany
    @JoinTable(name = "lecture_users",
            joinColumns = @JoinColumn(name = "lecture_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users = new ArrayList<>();//记录参加会议的人员；
}
