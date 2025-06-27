package com.kopo.learning;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
} 