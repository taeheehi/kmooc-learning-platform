package com.kopo.learning.admin.user;

import com.kopo.learning.common.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserService {
    public static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 30; // 30 minutes

    @Autowired
    private UserMapper userMapper;

    // ✅ 비밀번호 인코더
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 회원가입 처리
     */
    public User registerUser(String id, String name, String password, String role,
                             String phone, String email, String gender, String birth) {

        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");

        // 역할 한글→영어 변환
        String roleEng = role;
        switch (role) {
            case "학습자": roleEng = "STUDENT"; break;
            case "강의자": roleEng = "INSTRUCTOR"; break;
            case "관리자": roleEng = "ADMIN"; break;
        }

        // 핸드폰 번호 포맷팅 (010-1234-5678)
        String phoneFormatted = null;
        if (phoneNormalized != null && phoneNormalized.length() == 11 && phoneNormalized.startsWith("010")) {
            phoneFormatted = phoneNormalized.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }

        // ✅ 비밀번호 해시
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(hashedPassword);
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

        userMapper.save(user);
        return user;
    }

    /**
     * 로그인 처리
     */
    public User loginUser(String id, String password) {
        User user = userMapper.findById(id);
        if (user == null) {
            return null; // 사용자 없음
        }

        // 계정 잠금 상태 확인
        if (!user.isAccountNonLocked()) {
            if (user.getLockTime() != null && user.getLockTime().plusMinutes(LOCK_TIME_DURATION).isBefore(LocalDateTime.now())) {
                // 잠금 해제
                user.setAccountNonLocked(true);
                user.setLoginFailCount(0);
                user.setLockTime(null);
                userMapper.update(user);
            } else {
                throw new RuntimeException("계정이 잠겼습니다. 30분 후에 다시 시도해주세요.");
            }
        }

        // ✅ 비밀번호 매칭
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 로그인 성공
            user.setLoginFailCount(0);
            user.setLastLoginAt(LocalDateTime.now());
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            userMapper.update(user);
            return user;
        } else {
            // 로그인 실패
            int failCount = user.getLoginFailCount() == null ? 1 : user.getLoginFailCount() + 1;
            user.setLoginFailCount(failCount);

            if (failCount >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
            }
            userMapper.update(user);
            return null;
        }
    }

    /**
     * 사용자 정보 수정
     */
    public User updateUser(User user, String name, String oldPassword, String newPassword,
                           String phone, String email, String gender, String birth) {

        // ✅ 기존 비밀번호 확인
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return null; // 기존 비밀번호 불일치
        }

        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");
        user.setName(name);
        user.setPhone(phoneNormalized);
        user.setEmail(email);

        if (gender != null) user.setGender(gender);
        if (birth != null && !birth.isEmpty()) {
            user.setBirth(LocalDate.parse(birth));
        }

        // ✅ 새 비밀번호 변경 시 해시
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        user.setUpdatedAt(LocalDateTime.now());
        userMapper.update(user);
        return user;
    }

    // 기타 필요한 비즈니스 메서드 추가 가능
}
