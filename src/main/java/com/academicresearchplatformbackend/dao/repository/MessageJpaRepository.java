package com.academicresearchplatformbackend.dao.repository;

import com.academicresearchplatformbackend.dao.Message;
import com.academicresearchplatformbackend.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByTransmitter(User transmitter, Pageable pageable);

    Page<Message> findAllByReceiver(User receiver, Pageable pageable);

    Page<Message> findAllBySentTimeBetween(Timestamp small, Timestamp big, Pageable pageable);
}
