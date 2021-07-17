package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.AcademicResearchPlatformBackEndApplication;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("test001");
        user.setPassword("123456");
        userService.addUser(user);
    }
}
