package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.UserJpaRepository;
import com.academicresearchplatformbackend.service.MenuService;
import com.academicresearchplatformbackend.service.RoleService;
import com.academicresearchplatformbackend.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    //--------Dependency Injection--------------
    private UserJpaRepository userJpaRepository;
    private MenuService menuService;
    private RoleService roleService;
    @Autowired
    public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Autowired
    private void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    private void RoleService(RoleService roleService) {
        this.roleService = roleService;
    }
    //------------------------------------------
    @Override
    public Page<User> findAll(int page, int size) {
        return userJpaRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        System.out.println(user.toString());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times=2;//salt times;
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userJpaRepository.save(user);
        return userJpaRepository.save(user);
    }

    @Override
    public Boolean isExist(String username) {
        return userJpaRepository.findByUsername(username).isPresent();
    }
    @Override
    public User deleteUser(Long userId) {
        Optional<User> s = userJpaRepository.findById(userId);
        if (s.isPresent()) {
            userJpaRepository.deleteById(userId);
            return s.get();
        } else {
            return null;
        }
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> s = userJpaRepository.findByUsername(username);
        return s.orElse(null);
    }

    @Override
    public List<User> conditionalQuery(String[] conditions, String[] values) {
        List<User> results = null;
        System.out.println(Arrays.toString(conditions) + "\n" + Arrays.toString(values));
        int i = 0;
        for (String condition : conditions) {
            switch (condition) {
                case "id":
                    Optional<User> opUser = userJpaRepository.findById(Long.parseLong(values[i]));
                    if (opUser.isPresent()) {
                        if (i == 0) {
                            results = Arrays.asList(opUser.get());
                            return results;
                        }else{
                            results.retainAll(Arrays.asList(opUser.get()));
                            return results;
                        }
                    }

                case "name":
                    if (i == 0) {
                        results = userJpaRepository.findByName(values[i]);
                        i++;
                    }else{
                        results.retainAll(userJpaRepository.findByName(values[i++]));
                    }
                    break;/*
                case "role":
                    if (i == 0) {
                        results = userJpaRepository.findByRole(User.UserRole.valueOf(values[i]));
                        i++;
                    }else{
                        results.retainAll(userJpaRepository.findByRole(User.UserRole.valueOf(values[i++])));
                    }
                    break;*/
                case "email":
                    if (i == 0) {
                        results = userJpaRepository.findByEmail(values[i]);
                        i++;
                    }else{
                        results.retainAll(userJpaRepository.findByEmail(values[i++]));
                    }
                    break;
                case "gender":
                    if (i == 0) {
                        results = userJpaRepository.findByGender(Integer.parseInt(values[i]));
                        i++;
                    } else {
                        results.retainAll(userJpaRepository.findByGender(Integer.parseInt(values[i++])));

                    }
                    break;
            }
        }
        return results;
    }

    @Override
    public Boolean authentication(String username, String password) {
        Optional<User> user = userJpaRepository.findByUsername((username));
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        Optional<User> s = userJpaRepository.findById(id);
        if (s.isPresent()) {
            user.setPassword(s.get().getPassword());
            user.setSalt(s.get().getSalt());

            userJpaRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public List<Role> getRolesByUserId(Long id) {
        //Object currentUsername = SecurityUtils.getSubject().getPrincipal();
        //
        Optional<User> user = this.findById(id);
        return user.map(User::getRoles).orElse(null);

    }

    @Override
    public List<Menu> getMenusByUserId(Long id) {
        List<Role> roles = this.getRolesByUserId(id);
        Set<Menu> set = new HashSet<>();

        if (roles == null) {
            return null;
        } else {
            roles.forEach(i -> set.addAll(i.getMenus()));
        }

        List<Menu> menus = new ArrayList<>(set);

        menus.forEach(i->{
            List<Menu> children = menuService.getAllByParentId(i.getId());
            i.setChildren(children);
            System.out.println("dsadsfa");
        });
//      return all menus, including sub-menu
        menus.removeIf(i -> i.getIsRoot() != true);

        return menus;
    }

    @Override
    public boolean setRoles(Long uid, List<Role> roles) {
        Optional<User> op = this.findById(uid);
        if (op.isPresent()) {
            op.get().setRoles(roles);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRole(Long uid, Long rid) {
        try {
            User user = userJpaRepository.getById(uid);
            user.getRoles().add(roleService.getRoleById(rid));
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
