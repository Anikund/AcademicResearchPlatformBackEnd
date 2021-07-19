package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionServiceImplTest {
    @Autowired
    private PermissionService permissionService;

    @Test
    public void addPermission() {
        permissionService.addPermission("message:read");
        permissionService.addPermission("message:send");
        permissionService.addPermission("super");
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

    }
}
