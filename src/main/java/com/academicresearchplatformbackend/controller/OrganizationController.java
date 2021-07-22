package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Organization;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.OrganizationService;
import com.academicresearchplatformbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class OrganizationController {
    private OrganizationService organizationService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
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
            log.info("未登录用户请求获得组织信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("用户:" + subject.getPrincipal().toString() + "请求获得所有组织信息");
        return new ResponseEntity<>(organizationService.getAllPageable(page, size), HttpStatus.OK);
    }

    @GetMapping("/get/by/id/{id}")
    @ApiOperation("根据id获得所指向的组织，需要登录")
    public ResponseEntity<Organization> getById(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户请求获得组织信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Organization> op = organizationService.getById(id);
        if (op.isPresent()) {
            Organization result = op.get();
            log.info("用户:" + subject.getPrincipal().toString() + "请求获得组织(id=" + id + ")信息");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        log.info("不存在id为" + id + "的组织");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/by/name/{name}")
    @ApiOperation("根据名字获得所指向的组织，需要登录；注意组织的名字是唯一的")
    public ResponseEntity<Organization> getById(@PathVariable String name) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户请求获得组织信息");

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Organization> op = organizationService.getByName(name);
        if (op.isPresent()) {
            Organization result = op.get();
            log.info("用户:" + subject.getPrincipal().toString() + "请求获得组织(名字=" + name + ")信息");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        log.info("不存在名字为\"" + name + "\"的组织");

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/add")
    @ApiOperation("新增加一个组织，需要organization:create权限")
    public ResponseEntity<Organization> addOne(@RequestBody Organization organization) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("organization:create")) {
            log.info("用户：" + subject.getPrincipal().toString() + "创建新的组织");
            User user = userService.findByUsername(subject.getPrincipal().toString());
            organization.setPrincipal_id(user.getId());
            return new ResponseEntity<>(organizationService.addOne(organization), HttpStatus.OK);
        }
        log.info("当前用户无organization:create权限，无法创建组织");
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
                log.info("用户:" + subject.getPrincipal().toString() + "更新组织(id=" + id + ")的\"" + field + "\"字段的值为:" + value);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户:" + subject.getPrincipal().toString() + "更新组织信息失败");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("用户没有organization:update权限");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/principal/{oid}/{uid}")
    @ApiOperation("更新oid所指向的组织的负责人（或秘书）为uid所代表的用户，需要organization:setprincipal权限")
    public ResponseEntity<String> setPrincipal(@PathVariable Long oid,
                                               @PathVariable Long uid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("organization:setprincipal")) {
            organizationService.setPrincipal(oid, uid);
            log.info("用户:" + subject.getPrincipal().toString() + "更新组织(id=" + oid + ")的负责人为id=" + uid + "的用户");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.info("用户没有organization:setprincipal权限");

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/get/principal/{oid}")
    @ApiOperation("获得oid的组织的负责人信息")
    public ResponseEntity<User> getPrincipalByOrganization(@PathVariable Long oid) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户请求获得组织信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Organization> op = organizationService.getById(oid);
        if (op.isPresent()) {
            Organization result = op.get();
            log.info("用户:" + subject.getPrincipal().toString() + "请求获得组织(id=" + oid + ")的负责人");
            return new ResponseEntity<>(organizationService.getPrincipal(result.getId()), HttpStatus.OK);
        }
        log.info("不存在id为" + oid + "的组织");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
