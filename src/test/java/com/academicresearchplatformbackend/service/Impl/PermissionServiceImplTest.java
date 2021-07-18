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
        permissionService.addPermission("message:super");

    }
}
