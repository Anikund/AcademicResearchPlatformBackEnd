package com.academicresearchplatformbackend.dao;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
//    唯一id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    登录用户名
    @NotNull
    @Column(unique = true)
    private String username;
//    密码
    @NotNull
    private String password;
//    电话
    @NotNull
    private String tel;
//    姓名
    @NotNull
    private String name;
//    角色
    private UserRole role;

    public enum UserRole {
        RESEARCHER, SECRETARY, MINISTRY, OFFICER,VERTICAL_ADMIN, HORIZONTAL_ADMIN, OVERLORD
    }

//    邮箱
    @NotNull
    private String email;
    @NotNull
//    性别，0代表男性，1代表女性
    private Integer gender;


    @ManyToOne
    @JoinColumn(name="id")
    private Organization organization;
}
