package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.repository.PermissionJpaRepository;
import com.academicresearchplatformbackend.dao.repository.RoleJpaRepository;
import com.academicresearchplatformbackend.service.PermissionService;
import com.academicresearchplatformbackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PermissionServiceImpl implements PermissionService {
    private PermissionJpaRepository permissionJpaRepository;
    private RoleService roleService;
    @Autowired
    public void setPermissionJpaRepository(PermissionJpaRepository permissionJpaRepository) {
        this.permissionJpaRepository = permissionJpaRepository;


    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    public List<Permission> getAll() {
        return permissionJpaRepository.findAll();
    }

    public Page<Permission> getAllPageable(int page, int size) {
        return permissionJpaRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    public List<Permission> getByRoleId(Long roleId) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            return null;
        } else {
            return role.getPermissions();
        }
    }

    public Permission addPermission(String name) {
        if (permissionJpaRepository.findByName(name).isPresent()) {
            return null;
        }
        Permission p = new Permission();
        p.setName(name);
        return permissionJpaRepository.save(p);
    }

    @Override
    public Page<Permission> findAllPageable(int page, int size) {
        return permissionJpaRepository.findAll(PageRequest.of(page, size));
    }

}
