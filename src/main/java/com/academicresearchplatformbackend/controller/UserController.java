package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Api("UserController")
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

    public List<User> findAllPageable(@RequestParam int page, @RequestParam int size) {
        return userService.findAll(page, size);
    }

    @GetMapping("/findAll")
    @ApiOperation(value="获得所有用户")
    public List<User> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/delete/{userId}")
    @ApiOperation(value = "删除用户", notes = "删除用户，删除成功返回删除的用户信息与OK，否则返回NOT FOUND")
    @ApiImplicitParam(name = "userId", value = "用户唯一id", required = true)
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        User s = userService.deleteUser(userId);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<User>(s, HttpStatus.OK);
        }
    }
}
