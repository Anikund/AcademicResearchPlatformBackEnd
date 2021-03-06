package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
