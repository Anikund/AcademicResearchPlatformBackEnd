package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Override
    Page<User> findAll(Pageable pageable);

    List<User> findByName(String name);

    //List<User> findByRole(User.UserRole role);

    List<User> findByEmail(String email);

    List<User> findByGender(Integer gender);

    Optional<User> findByUsername(String username);
}
