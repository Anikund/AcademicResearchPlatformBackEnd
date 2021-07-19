package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.OpenFilesEvent;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Api("UserController, 此下所有api均需要super权限")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findAllPageable")
    @ApiOperation(value = "获得所有用户", notes = "分页方法，必须传入page与size,按照Id升序排列")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页数", required = true),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true)})

    public Page<User> findAllPageable(@RequestParam int page, @RequestParam int size) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return null;
        }
        return userService.findAll(page, size);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "获得所有用户")
    public List<User> findAll() {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return null;
        }
        return userService.findAll();
    }

    @DeleteMapping("/delete/{userId}")
    @ApiOperation(value = "删除用户", notes = "删除用户，删除成功返回删除的用户信息与OK，否则返回NOT FOUND")
    @ApiImplicitParam(name = "userId", value = "用户唯一id", required = true)
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User s = userService.deleteUser(userId);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }

    @PostMapping("/add")
    @ApiOperation("添加一个用户")
    @ApiImplicitParam(name = "user", value = "要添加的用户信息")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User s = userService.addUser(user);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }

    @GetMapping("/find/{id}")
    @ApiOperation("根据id查询用户")
    @ApiImplicitParam(name = "id", value = "id")
    public ResponseEntity<User> getOneById(@PathVariable Long id) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> s = userService.findById(id);
        if (s.isPresent()) {
            return new ResponseEntity<>(s.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/query/{conditions}/{values}")
    @ApiOperation("条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "conditions", value = "查询条件", required = true),
            @ApiImplicitParam(name = "values", value = "查询值", required = true)
    })
    public List<User> conditionalQuery(@PathVariable String[] conditions, @PathVariable String[] values) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return null;
        }
        return userService.conditionalQuery(conditions, values);
    }


    @PutMapping("/update")
    @ApiOperation("更新指定的用户，需要传入整个用户信息，先查该用户，再在此基础上改，通过直接赋值实现的更新")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<User> opUser = userService.findById(user.getId());
        if (opUser.isPresent()) {
            User s = userService.updateUser(user);
            if (s == null) {
                return new ResponseEntity<>("更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("无该用户", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/roles/{uid}")
    @ApiOperation("更新uid所指向的用户的角色列表")
    @ApiImplicitParam(name = "roles", value = "要更新成的角色列表，先查角色，查完了选，选完了原封不动传回")
    public ResponseEntity<String> updateRolesById(@PathVariable Long uid,
                                                  @RequestBody List<Role> roles) {
        if (!SecurityUtils.getSubject().isPermitted("super")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.setRoles(uid, roles);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
