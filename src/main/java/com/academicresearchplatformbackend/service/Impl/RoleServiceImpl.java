package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.repository.RoleJpaRepository;
import com.academicresearchplatformbackend.service.PermissionService;
import com.academicresearchplatformbackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    public void setRoleJpaRepository(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    public List<Role> getAll() {
        return roleJpaRepository.findAll();
    }

    public Page<Role> getAllPageable(int page, int size) {
        return roleJpaRepository.findAll(PageRequest.of(page, size));

    }

    public Role getRoleById(Long id) {
        if (roleJpaRepository.findById(id).isPresent()) {
            return roleJpaRepository.findById(id).get();
        } else {
            return null;
        }
    }




}
