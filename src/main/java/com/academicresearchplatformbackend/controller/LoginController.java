package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.MO.SessionWrapper;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.functors.ExceptionPredicate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.WebSessionManager;
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
@Api("Login Controller")
@Log4j2
public class LoginController {
    private UserService userService;

    private MyUtils myUtils;

    @Autowired
    public void setMyUtils(MyUtils myUtils) {
        this.myUtils = myUtils;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        String username = user.getUsername();
        log.info("用户名" + username + "尝试登录");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, user.getPassword());
        token.isRememberMe();
        //Object currentUser = subject.getPrincipal();
        //SessionKey key = new DefaultSessionManager().getSessionDAO().getActiveSessions();
        try {
            subject.login(token);
            subject.getSession().setAttribute("user", userService.findByUsername(username));
            User foundUser = userService.findByUsername(username);
            //currentUser = subject.getPrincipal();
            log.info("用户" + foundUser.getUsername() + "，姓名" + foundUser.getName() + "登录成功");
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        } catch (UnknownAccountException uae) {
            System.out.println("unknown account");
            log.info("未知账号");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IncorrectCredentialsException incorrectCredentialsException) {
            System.out.println("incorrect password");
            log.info("密码错误");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            System.out.println("other");
            log.info("其他原因登录失败");
            System.out.println(e.toString());
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
            log.info("无用户登录，尝试登出，失败");
            return new ResponseEntity<>("登出失败！", HttpStatus.CONFLICT);
        }
        if (username.equals(currentUser.getPrincipal().toString())) {
            currentUser.logout();
            log.info("用户" + username + "登出成功");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("用户登出失败");

            return new ResponseEntity<>("登出失败！", HttpStatus.CONFLICT);
        }

    }
}
