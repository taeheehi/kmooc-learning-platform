package com.kopo.learning;

import com.kopo.learning.common.vo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


// 로그인한 사용자 정보를 전역적으로 뷰에 전달해주는 공통 설정 파일
@ControllerAdvice
public class GlobalUserAdvice {
    @ModelAttribute("user")
    public User addUserToModel(HttpSession session) {
        return (User) session.getAttribute("user");
    }
} 