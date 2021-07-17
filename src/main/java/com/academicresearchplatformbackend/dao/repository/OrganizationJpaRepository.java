package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationJpaRepository extends JpaRepository<Organization, Long> {
}
