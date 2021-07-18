package com.academicresearchplatformbackend.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

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
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String password;
    //    电话

    private String tel;
    //    姓名

    private String name;
    //    角色
    private UserRole role;

    public enum UserRole {
        RESEARCHER, SECRETARY, MINISTRY, OFFICER, VERTICAL_ADMIN, HORIZONTAL_ADMIN, OVERLORD
    }

    //    邮箱
    @NotNull
    private String email;
    @NotNull
//    性别，0代表男性，1代表女性
    private Integer gender;
    private String job;//职位
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToMany
    @JoinTable(name="user_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;
}
