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
public class LearningRoadmap {
    private Long id;
    private String userId;        // 사용자 ID
    private String roadmapName;   // 로드맵 이름
    private String description;   // 로드맵 설명
    private String status;        // ACTIVE, INACTIVE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 로드맵 단계 정보
    private Long stepId;
    private Integer stepOrder;    // 단계 순서
    private Long courseId;        // 해당 단계의 강의 ID
    private String stepStatus;    // NOT_STARTED, IN_PROGRESS, COMPLETED
    
    // 조인을 위한 추가 정보
    private String courseName;
    private String teacherName;
    private String thumbnailUrl;
    private String category;
} 