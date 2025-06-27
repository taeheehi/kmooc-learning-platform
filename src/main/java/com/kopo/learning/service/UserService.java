package com.kopo.learning.service;

import com.kopo.learning.model.User;
import com.kopo.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String id, String name, String password, String role, String phone, String email, String gender, String birth) {
        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");
        // 역할 한글→영어 변환
        String roleEng = role;
        switch (role) {
            case "학습자": roleEng = "STUDENT"; break;
            case "강의자": roleEng = "INSTRUCTOR"; break;
            case "관리자": roleEng = "ADMIN"; break;
        }
        // 핸드폰 번호 포맷팅 (010-1234-1234)
        String phoneFormatted = null;
        if (phoneNormalized != null && phoneNormalized.length() == 11 && phoneNormalized.startsWith("010")) {
            phoneFormatted = phoneNormalized.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        user.setRole(roleEng);
        user.setPhone(phoneFormatted);
        user.setEmail(email);
        user.setGender(gender);
        if (birth != null && !birth.isEmpty()) {
            user.setBirth(LocalDate.parse(birth));
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmailVerified(false);
        return userRepository.save(user);
    }

    public User loginUser(String id, String password) {
        User user = userRepository.findById(id);
        if (user != null && user.getPassword().equals(password)) {
            user.setLoginFailCount(0);
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            return user;
        } else {
            if (user != null) {
                int fail = user.getLoginFailCount() == null ? 1 : user.getLoginFailCount() + 1;
                user.setLoginFailCount(fail);
                userRepository.save(user);
            }
            return null;
        }
    }

    public User updateUser(User user, String name, String oldPassword, String newPassword, String phone, String email, String gender, String birth) {
        if (!user.getPassword().equals(oldPassword)) {
            return null; // 비밀번호 불일치
        }
        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");
        user.setName(name);
        user.setPhone(phoneNormalized);
        user.setEmail(email);
        if (gender != null) user.setGender(gender);
        if (birth != null && !birth.isEmpty()) {
            user.setBirth(LocalDate.parse(birth));
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // 기타 필요한 비즈니스 로직 메서드 추가 가능
} 