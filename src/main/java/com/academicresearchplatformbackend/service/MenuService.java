package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Menu;

import java.util.List;

public interface MenuService {
    /**
     * get all menus whose parent menu is the menu with the given pid
     * @param pid
     * @return
     */
    List<Menu> getAllByParentId(Long pid);

    List<Menu> getAllMenus();
}
