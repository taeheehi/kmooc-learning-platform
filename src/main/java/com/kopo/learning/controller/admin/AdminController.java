package com.kopo.learning.controller.admin;

import com.kopo.learning.model.User;
import com.kopo.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin/admin_users";
    }
    
    @GetMapping("/admin/stats")
    public String adminStats(Model model, @RequestParam(name = "period", defaultValue = "7days") String period) {
        // --- 1. KPI 데이터 ---
        long totalUsers = userRepository.count();
        long todaySignups = userRepository.countByCreatedAtAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        long activeUsers = userRepository.countByLastLoginAtAfter(LocalDateTime.now().minusMinutes(30));

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("todaySignups", todaySignups);
        model.addAttribute("activeUsers", activeUsers);

        // --- 2. 회원 상태 (활성/휴면/탈퇴) ---
        long activeStatusUsers = userRepository.countByLastLoginAtAfter(LocalDateTime.now().minusDays(30));
        long dormantUsers = totalUsers - activeStatusUsers; // 전체 - 활성 = 휴면 (탈퇴 회원은 없다고 가정)
        // long withdrawnUsers = userRepository.countByStatus("WITHDRAWN"); // 탈퇴 기능 구현 시 사용
        
        model.addAttribute("activeStatusUsers", activeStatusUsers);
        model.addAttribute("dormantUsers", dormantUsers);
        // model.addAttribute("withdrawnUsers", withdrawnUsers);

        // --- 3. 24시간 접속 트렌드 (선 그래프) ---
        Map<Integer, Long> hourlyLogins = userRepository.countLoginsByHourAfter(LocalDateTime.now().minusHours(24)).stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).intValue(),
                        row -> ((Number) row[1]).longValue()
                ));
        // 0-23시까지 모든 시간에 대한 기본값 0을 설정
        Map<Integer, Long> fullHourlyLogins = new LinkedHashMap<>();
        for (int i = 0; i < 24; i++) {
            fullHourlyLogins.put(i, 0L);
        }
        fullHourlyLogins.putAll(hourlyLogins);
        model.addAttribute("hourlyLogins", fullHourlyLogins);

        // --- 4. 역할별 분포 (파이 차트) ---
        Map<String, Long> roleCounts = userRepository.countByRole().stream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (Long) row[1]));
        model.addAttribute("roleCounts", roleCounts);

        // --- 5. 기간별 신규 가입자/휴면 전환자 (막대+꺾은선 그래프) ---
        Map<String, Long> signupTrendData = new LinkedHashMap<>();
        Map<String, Long> signupDormantTrendData = new LinkedHashMap<>();
        if ("year".equals(period)) {
            // 월별로 그룹화
            for (int i = 1; i <= 12; i++) {
                LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), i, 1);
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                long signupCount = userRepository.findAll().stream()
                        .filter(u -> u.getCreatedAt() != null && !u.getCreatedAt().toLocalDate().isBefore(startDate) && !u.getCreatedAt().toLocalDate().isAfter(endDate))
                        .count();
                signupTrendData.put(i + "월", signupCount);

                // 휴면 전환자: 해당 월에 30일 연속 미접속이 된 회원
                long dormantCount = userRepository.findAll().stream()
                        .filter(u -> u.getLastLoginAt() != null)
                        .filter(u -> {
                            LocalDateTime lastLogin = u.getLastLoginAt();
                            LocalDate dormantDate = lastLogin.toLocalDate().plusDays(30);
                            return !dormantDate.isBefore(startDate) && !dormantDate.isAfter(endDate);
                        })
                        .count();
                signupDormantTrendData.put(i + "월", dormantCount);
            }
        } else {
            int days = "30days".equals(period) ? 30 : 7;
            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                long signupCount = userRepository.findAll().stream()
                        .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().toLocalDate().isEqual(date))
                        .count();
                signupTrendData.put(date.format(DateTimeFormatter.ofPattern("MM-dd")), signupCount);

                // 휴면 전환자: (마지막 로그인일 + 30일)이 해당 날짜와 같은 회원
                long dormantCount = userRepository.findAll().stream()
                        .filter(u -> u.getLastLoginAt() != null)
                        .filter(u -> u.getLastLoginAt().toLocalDate().plusDays(30).isEqual(date))
                        .count();
                signupDormantTrendData.put(date.format(DateTimeFormatter.ofPattern("MM-dd")), dormantCount);
            }
        }
        model.addAttribute("signupTrendData", signupTrendData);
        model.addAttribute("signupDormantTrendData", signupDormantTrendData);

        // --- 6. 성별/연령대별 통계 ---
        Map<String, Long> genderCounts = userRepository.countByGender().stream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (Long) row[1]));
        
        Map<String, Long> ageGroupCounts = new LinkedHashMap<>();
        ageGroupCounts.put("10대", 0L);
        ageGroupCounts.put("20대", 0L);
        ageGroupCounts.put("30대", 0L);
        ageGroupCounts.put("40대", 0L);
        ageGroupCounts.put("50대 이상", 0L);
        ageGroupCounts.put("미기입", 0L);

        userRepository.findAll().forEach(user -> {
            if (user.getBirth() == null) {
                ageGroupCounts.merge("미기입", 1L, Long::sum);
                return;
            }
            int age = LocalDate.now().getYear() - user.getBirth().getYear();
            if (age < 20) ageGroupCounts.merge("10대", 1L, Long::sum);
            else if (age < 30) ageGroupCounts.merge("20대", 1L, Long::sum);
            else if (age < 40) ageGroupCounts.merge("30대", 1L, Long::sum);
            else if (age < 50) ageGroupCounts.merge("40대", 1L, Long::sum);
            else ageGroupCounts.merge("50대 이상", 1L, Long::sum);
        });

        model.addAttribute("genderCounts", genderCounts);
        model.addAttribute("ageGroupCounts", ageGroupCounts);

        return "admin/admin_stats";
    }

    @PostMapping("/admin/users/toggle-lock")
    public String toggleLock(@RequestParam("userId") String userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setAccountNonLocked(!user.isAccountNonLocked());
            if (user.isAccountNonLocked()) {
                user.setLoginFailCount(0);
                user.setLockTime(null);
            } else {
                user.setLockTime(LocalDateTime.now());
            }
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/update-memo")
    public String updateMemo(@RequestParam("userId") String userId, @RequestParam("memo") String memo) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setAdminMemo(memo);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
    
    // 관리자 관련 기능 (회원관리, 공지사항 관리, 통계 등)
} 