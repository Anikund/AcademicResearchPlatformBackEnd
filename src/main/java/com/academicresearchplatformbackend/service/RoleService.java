package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Page<Role> getAllPageable(int page, int size);

    Role getRoleById(Long id);
}
