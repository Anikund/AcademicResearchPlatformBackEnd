package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationJpaRepository extends JpaRepository<Organization, Long> {
//    @Override
    Optional<Organization> findByName(String name);
}
