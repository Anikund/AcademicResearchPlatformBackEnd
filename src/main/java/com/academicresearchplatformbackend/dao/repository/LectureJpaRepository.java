package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    Page<Lecture> findAllByType(String type, Pageable pageable);

    Page<Lecture> findAllByLevel(String level, Pageable pageable);


}
