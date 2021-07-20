package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/permission")
@Api("Permission Controller，里面的所有api都需要登录")
@Log4j2
public class PermissionController {
    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有的权限列表，谁都能获得")
    public ResponseEntity<Page<Permission>> getAllPermissionsPageable(
            @RequestParam int page, @RequestParam int size
    ) {/*
        if (SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(permissionService.findAllPageable(page, size), HttpStatus.OK);
        }*/
        log.info("所有权限列表被请求");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
