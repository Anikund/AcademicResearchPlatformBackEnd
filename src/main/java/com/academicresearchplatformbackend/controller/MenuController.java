package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.MenuService;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/menus")
public class MenuController {
    private MenuService menuService;
    private UserService userService;
    private MyUtils authenticationUtils;

    @Autowired
    public void setAuthenticationUtils(MyUtils authenticationUtils) {
        this.authenticationUtils = authenticationUtils;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/all")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }
    //@RequiresRoles("common researcher")
    @GetMapping("/menusByUser")
    public ResponseEntity<List<Menu>> getMenusByCurrentUser() {
        //MyUtils authenticationUtils = new MyUtils();
        Subject subject = SecurityUtils.getSubject();
        /*
        if (!subject.hasRole("researcher")) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!subject.isPermitted("test")) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

         */
        MiddleResult<User> mr = authenticationUtils.getCurrentAuthenticatedUser();
        if (!mr.isSuccess()) {
            //
            // return new ResponseEntity<String>(mr.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else{
            User user = mr.getData();
            List<Menu> result = userService.getMenusByUserId(user.getId());
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }

//        TBD
    }
}
