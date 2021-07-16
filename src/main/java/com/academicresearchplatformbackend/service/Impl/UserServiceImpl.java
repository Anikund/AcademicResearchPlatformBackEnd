package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.UserJpaRepository;
import com.academicresearchplatformbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    //--------Dependency Injection--------------
    private UserJpaRepository userJpaRepository;

    @Autowired
    public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    //------------------------------------------
    @Override
    public List<User> findAll(int page, int size) {
        return userJpaRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).toList();
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        return userJpaRepository.save(user);
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
}
