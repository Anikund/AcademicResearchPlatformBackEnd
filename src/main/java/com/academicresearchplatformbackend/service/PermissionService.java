package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Permission;
import org.springframework.data.domain.Page;

public interface PermissionService {
    Permission addPermission(String name);
    Page<Permission> findAllPageable(int page, int size);
}
