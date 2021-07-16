package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.User;

import java.util.List;

public interface UserService {
    /**
     * 获得所有用户，分页方式
     * @param page 页数
     * @param size 每页数量
     * @return 请求页的用户列表
     */
    List<User> findAll(int page, int size);
    List<User> findAll();
    /**
     * 添加一个用户
     * @param user
     * @return 注册成功则返回用户，否则返回null
     */
    User addUser(User user);

    /**
     * 删除指定用户，用唯一id删除
     * @param userId 用户id
     * @return 删除成功则返回被删除的用户，否则返回null
     */
    User deleteUser(Long userId);
}
