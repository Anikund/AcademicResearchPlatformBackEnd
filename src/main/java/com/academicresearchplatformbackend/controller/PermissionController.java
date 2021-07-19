package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/permission")
@Api("RoleController，里面所有的api都需要super权限")
public class PermissionController {
    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有的权限列表")
    public ResponseEntity<Page<Permission>> getAllPermissionsPageable(
            @RequestParam int page, @RequestParam int size
    ) {
        if (SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(permissionService.findAllPageable(page, size), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
