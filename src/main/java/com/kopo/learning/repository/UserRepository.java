package com.kopo.learning.repository;

import com.kopo.learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
} 