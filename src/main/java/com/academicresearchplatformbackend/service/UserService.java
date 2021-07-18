package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * 获得所有用户，分页方式
     * @param page 页数
     * @param size 每页数量
     * @return 请求页的用户列表
     */
    Page<User> findAll(int page, int size);
    List<User> findAll();
    /**
     * 添加一个用户
     * @param user user
     * @return 注册成功则返回用户，否则返回null
     */
    User addUser(User user);

    /**
     * 删除指定用户，用唯一id删除
     * @param userId 用户id
     * @return 删除成功则返回被删除的用户，否则返回null
     */
    User deleteUser(Long userId);

    User findByUsername(String username);
    Boolean isExist(String username);
    List<User> conditionalQuery(String[] conditions, String[] values);

    Boolean authentication(String username, String password);

    User updateUser(User user);

    Optional<User> findById(Long id);


    //    menus
    List<Role> getRolesByUserId(Long id);

    List<Menu> getMenusByUserId(Long id);
}
