package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    /**
     * get all menus whose parent menu is the menu with the given pid
     * @param pid the id of the parent menu
     * @return a list of menus that are direct children of the menu specified by the given id
     */
    List<Menu> getAllByParentId(Long pid);

    List<Menu> getAllMenus();
    Page<Menu> findAllPageable(int page, int size);
}
