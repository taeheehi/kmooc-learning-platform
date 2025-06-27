package com.kopo.learning.repository;

import com.kopo.learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);

    long countByCreatedAtAfter(LocalDateTime dateTime);
    long countByLastLoginAtAfter(LocalDateTime dateTime);
    long countByStatus(String status);
    long countByLastLoginAtBeforeOrLastLoginAtIsNull(LocalDateTime dateTime);

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countByRole();

    @Query(value = "SELECT HOUR(last_login_at) as hour, COUNT(*) as count FROM user WHERE last_login_at >= :since GROUP BY HOUR(last_login_at)", nativeQuery = true)
    List<Object[]> countLoginsByHourAfter(@Param("since") LocalDateTime since);

    @Query("SELECT u.gender, COUNT(u) FROM User u WHERE u.gender IS NOT NULL GROUP BY u.gender")
    List<Object[]> countByGender();
} 