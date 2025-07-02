package com.kopo.learning.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    private Long id;
    private String userId;        // 수강생 ID
    private Long courseId;        // 강의 ID
    private String status;        // ENROLLED, IN_PROGRESS, COMPLETED, DROPPED
    private Integer progress;     // 진행률 (0-100)
    private LocalDateTime enrolledAt;    // 수강 신청일
    private LocalDateTime startedAt;     // 학습 시작일
    private LocalDateTime completedAt;   // 수료일
    private LocalDateTime lastAccessedAt; // 마지막 접속일
    
    // 조인을 위한 추가 정보
    private String courseName;
    private String teacherName;
    private String thumbnailUrl;
    private String category;
} 