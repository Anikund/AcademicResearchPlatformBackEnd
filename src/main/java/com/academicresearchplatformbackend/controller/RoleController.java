package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.mapping.Subclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/role")
@Api("RoleController，里面所有的api都需要super权限")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有当前的角色")
    public ResponseEntity<Page<Role>> getAllRolesPageable(@RequestParam int page,
                                                          @RequestParam int size) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {
            return new ResponseEntity<>(roleService.getAllPageable(page, size), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/all/{id}")
    @ApiOperation("获得id所指定的角色")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {
            Role requestedRole = roleService.getRoleById(id);
            if (requestedRole == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(requestedRole, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


    @PostMapping("/add")
    @ApiOperation("新加一个角色，，注意角色一旦添加不可删除")
    @ApiImplicitParam(name = "role", value = "要添加的角色实例")
    public ResponseEntity<Role> addNewRole(@RequestBody Role role) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {

            return new ResponseEntity<>(roleService.addOne(role),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update")
    @ApiOperation("修改传来的角色信息，需要传入整个角色，这是直接通过赋值改的，不是一个字段一个字段改的，所以先通过id获取再在那个基础上修改")
    @ApiImplicitParam(name = "role", value = "修改后的角色信息")
    public ResponseEntity<String> updateRole(Role role) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {
            if (roleService.updateRole(role)) {

                return new ResponseEntity<>( HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/permissions/{id}")
    @ApiOperation("根据id修改对应的角色的权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要修改权限的角色id"),
            @ApiImplicitParam(name = "permissions", value = "修改后的权限数组")
    })
    public ResponseEntity<String> updateRolePermissions(@PathVariable Long id, @RequestBody List<Permission> permissions) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {
            if (roleService.updateRolePermissions(id, permissions)) {

                return new ResponseEntity<>( HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/menus/{id}")
    @ApiOperation("根据id修改对应的角色的菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要修改权限的角色id"),
            @ApiImplicitParam(name = "menus", value = "修改后的菜单数组")
    })
    public ResponseEntity<String> updateRoleMenus(@PathVariable Long id, @RequestBody List<Menu> menus) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isPermitted("super")) {
            if (roleService.setRoleMenus(id, menus)) {

                return new ResponseEntity<>( HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
