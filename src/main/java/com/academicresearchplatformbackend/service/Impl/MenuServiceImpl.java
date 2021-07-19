package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.repository.MenuJpaRepository;
import com.academicresearchplatformbackend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class MenuServiceImpl implements MenuService {

    private MenuJpaRepository menuJpaRepository;

    @Autowired
    public void setMenuJpaRepository(MenuJpaRepository menuJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
    }

    @Override
    public List<Menu> getAllByParentId(Long pid) {
        List<Menu> result = new ArrayList<>();
        List<Menu> allMenus = menuJpaRepository.findAll();
        if (allMenus == null) {
            return null;
        } else {
            allMenus.forEach(i->{
                if (i.getParentMenu()!=null && i.getParentMenu().getId() == pid) {
                    result.add(i);
                }
            });
            return result;
        }
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuJpaRepository.findAll();
    }

    @Override
    public Page<Menu> findAllPageable(int page, int size) {
        return menuJpaRepository.findAll(PageRequest.of(page, size));
    }
}
