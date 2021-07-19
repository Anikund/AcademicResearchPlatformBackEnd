package com.academicresearchplatformbackend.service;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.Lecture;
import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    Lecture addOne(Lecture lecture);

    boolean addResource(Long id, FileResource resource);

    boolean addUser(Long id, User user);

    boolean update(Long id, String field, String value);

    List<Lecture> findAll();

    Page<Lecture> findAllPageable(int page, int size);

    Page<Lecture> findAllByType(String type,int page, int size);

    Page<Lecture> findAllByLevel(String level,int page, int size);

    boolean deleteById(Long id);

    Optional<Lecture> getById(Long id);
}
