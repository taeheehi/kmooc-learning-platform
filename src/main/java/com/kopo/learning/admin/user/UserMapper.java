package com.kopo.learning.admin.user;

import com.kopo.learning.common.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    User findById(@Param("id") String id);
    boolean existsByPhone(@Param("phone") String phone);
    boolean existsByEmail(@Param("email") String email);

    long countByCreatedAtAfter(@Param("dateTime") LocalDateTime dateTime);
    long countByLastLoginAtAfter(@Param("dateTime") LocalDateTime dateTime);
    long countByStatus(@Param("status") String status);
    long countByLastLoginAtBeforeOrLastLoginAtIsNull(@Param("dateTime") LocalDateTime dateTime);

    List<Object[]> countByRole();
    List<Object[]> countLoginsByHourAfter(@Param("since") LocalDateTime since);
    List<Object[]> countByGender();

    List<User> findAll();
    int save(User user);
    int update(User user);
} 