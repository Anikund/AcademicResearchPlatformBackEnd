package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.Permission;
import com.academicresearchplatformbackend.dao.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Page<Role> getAllPageable(int page, int size);

    Role getRoleById(Long id);

    Role addOne(Role role);

    boolean updateRole(Role role);
    boolean updateRolePermissions(Long id, List<Permission> permissions);
    boolean setRoleMenus(Long id, List<Menu> menus);
}
