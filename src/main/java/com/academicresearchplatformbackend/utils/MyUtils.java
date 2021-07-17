package com.academicresearchplatformbackend.utils;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MyUtils {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MiddleResult<User> getCurrentAuthenticatedUser() {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return new MiddleResult<User>("无权限").isFailed();//unauthenticated user
        }
        Object principal = currentUser.getPrincipal();
        if (principal == null) {
            return new MiddleResult<User>("无法访问").isFailed();//no logged in user
        } else {
            String username = principal.toString();
            User user = userService.findByUsername(username);
            return new MiddleResult<>(user);
        }
    }
}
