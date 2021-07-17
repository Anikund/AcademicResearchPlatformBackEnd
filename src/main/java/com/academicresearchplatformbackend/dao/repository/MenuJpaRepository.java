package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

}
