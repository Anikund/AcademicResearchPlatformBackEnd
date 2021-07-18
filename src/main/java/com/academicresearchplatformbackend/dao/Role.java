package com.academicresearchplatformbackend.dao;

import lombok.Data;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int level;
    private String description;

    @ManyToMany
    @JoinTable(name="role_menus",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="menu_id"))
    private List<Menu> menus = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="role_permissions",
    joinColumns = @JoinColumn(name="role_id"),
    inverseJoinColumns = @JoinColumn(name="p_id"))
    private List<Permission> permissions = new ArrayList<>();
}
