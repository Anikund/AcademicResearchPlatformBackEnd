package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.Impl.MenuServiceImpl;
import com.academicresearchplatformbackend.service.MenuService;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.AuthenticationUtils;
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

    @GetMapping("/menusByUser")
    public ResponseEntity<List<Menu>> getMenusByCurrentUser() {
        AuthenticationUtils authenticationUtils = new AuthenticationUtils();
        MiddleResult<User> mr = authenticationUtils.getCurrentAuthenticatedUser();
        if (mr.isSuccess() == false) {
            //
            // return new ResponseEntity<String>(mr.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }else{
            User user = mr.getData();
            List<Menu> result = userService.getMenus();
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            } else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }

//        TBD
    }
}
