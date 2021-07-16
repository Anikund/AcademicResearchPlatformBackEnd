package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Override
    Page<User> findAll(Pageable pageable);
}
