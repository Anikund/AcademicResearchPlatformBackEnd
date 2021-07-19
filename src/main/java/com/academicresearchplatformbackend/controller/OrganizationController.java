package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Organization;
import com.academicresearchplatformbackend.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/organization")
@Api("OrganizationController，里面所有api都需要登录")
public class OrganizationController {
    private OrganizationService organizationService;

    @Autowired
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有组织的信息，需要登录")
    public ResponseEntity<Page<Organization>> getAllPageable(@RequestParam int page,
                                                             @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(organizationService.getAllPageable(page, size), HttpStatus.OK);
    }

    @GetMapping("/get/by/id/{id}")
    @ApiOperation("根据id获得所指向的组织，需要登录")
    public ResponseEntity<Organization> getById(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Organization> op = organizationService.getById(id);
        if (op.isPresent()) {
            Organization result = op.get();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/by/name/{name}")
    @ApiOperation("根据名字获得所指向的组织，需要登录；注意组织的名字是唯一的")
    public ResponseEntity<Organization> getById(@PathVariable String name) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Organization> op = organizationService.getByName(name);
        if (op.isPresent()) {
            Organization result = op.get();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/add")
    @ApiOperation("新增加一个组织，需要organization:create权限")
    public ResponseEntity<Organization> addOne(@RequestBody Organization organization) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("organization:create")) {
            return new ResponseEntity<>(organizationService.addOne(organization), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/{id}/{field}/{value}")
    @ApiOperation("更新id所指向的组织的信息,需要organization:update权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组织id"),
            @ApiImplicitParam(name = "field", value = "要更新的字段名，可以为name,location, tel, description, regulations 之一"),
            @ApiImplicitParam(name = "value", value = "所要更新成的值")
    })
    public ResponseEntity<String> updateOrganization(@PathVariable Long id,
                                                     @PathVariable String field,
                                                     @PathVariable String value) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("organization:update")) {
            if (organizationService.update(id, field, value)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/principal/{oid}/{uid}")
    @ApiOperation("更新oid所指向的组织的负责人（或秘书）为uid所代表的用户，需要organization:setprincipal权限")
    public ResponseEntity<String> setPrincipal(@PathVariable Long oid,
                                               @PathVariable Long uid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("organization:setprincipal")) {
            organizationService.setPrincipal(oid, uid);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
