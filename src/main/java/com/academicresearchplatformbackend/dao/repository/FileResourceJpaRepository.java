package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileResourceJpaRepository extends JpaRepository<FileResource, Long> {

}
