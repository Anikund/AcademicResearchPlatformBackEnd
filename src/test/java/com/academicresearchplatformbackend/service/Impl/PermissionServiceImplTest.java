package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.PermissionService;
import com.academicresearchplatformbackend.service.RoleService;
import com.academicresearchplatformbackend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionServiceImplTest {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Test
    public void addPermissionAndAdminRoleAndAdminUser() {
        permissionService.addPermission("message:read");
        permissionService.addPermission("message:send");
        Permission adminPermission = permissionService.addPermission("super");
        permissionService.addPermission("project:create");
        permissionService.addPermission("project:update");
        permissionService.addPermission("project:censor");
        permissionService.addPermission("project:terminate");
        permissionService.addPermission("project:assign");
        permissionService.addPermission("project:consumefund");
        permissionService.addPermission("project:view");
        permissionService.addPermission("organization:create");
        permissionService.addPermission("organization:update");
        permissionService.addPermission("organization:setprincipal");
        permissionService.addPermission("feat:delete");
        permissionService.addPermission("feat:update");
        permissionService.addPermission("feat:create");
        permissionService.addPermission("resource:update");
        permissionService.addPermission("resource:create");
        permissionService.addPermission("lecture:delete");
        permissionService.addPermission("lecture:create");
        permissionService.addPermission("lecture:attend");
        permissionService.addPermission("lecture:update");
        Role adminRole = new Role();
        adminRole.setPermissions(Arrays.asList(adminPermission));
        adminRole.setName("admin");

        roleService.addOne(adminRole);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin");
        adminUser.setRoles(Arrays.asList(adminRole));
        userService.addUser(adminUser);
    }
}
