package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import org.apache.commons.collections.functors.ExceptionPredicate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    @Autowired
    private MyUtils myUtils;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        String username = user.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, user.getPassword());
        token.isRememberMe();
        //Object currentUser = subject.getPrincipal();
        try {
            subject.login(token);
            subject.getSession().setAttribute("user", userService.findByUsername(username));
            User foundUser = userService.findByUsername(username);
            //currentUser = subject.getPrincipal();

            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        } catch (UnknownAccountException uae) {
            System.out.println("unknown account");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IncorrectCredentialsException incorrectCredentialsException) {
            System.out.println("incorrect password");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            System.out.println("other");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/logout/{username}")
    public ResponseEntity<String> logout(@PathVariable String username) {
        /*
        MiddleResult<User> mr = myUtils.getCurrentAuthenticatedUser();
        //Subject subject = null;
        if (mr.isSuccess()) {
            User user = mr.getData();
        }*/
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipal() == null) {
            return new ResponseEntity<>("登出失败！", HttpStatus.CONFLICT);
        }
        if (username.equals(currentUser.getPrincipal().toString())) {
            currentUser.logout();
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("登出失败！", HttpStatus.CONFLICT);
        }

    }
}
