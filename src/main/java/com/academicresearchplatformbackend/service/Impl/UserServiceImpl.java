package com.academicresearchplatformbackend.service.Impl;

import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.dao.repository.UserJpaRepository;
import com.academicresearchplatformbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        System.out.println(user.toString());
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

    @Override
    public List<User> conditionalQuery(String[] conditions, String[] values) {
        List<User> results = null;
        System.out.println(Arrays.toString(conditions) + "\n" + values);
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
                    break;
                case "role":
                    if (i == 0) {
                        results = userJpaRepository.findByRole(User.UserRole.valueOf(values[i]));
                        i++;
                    }else{
                        results.retainAll(userJpaRepository.findByRole(User.UserRole.valueOf(values[i++])));
                    }
                    break;
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
}
