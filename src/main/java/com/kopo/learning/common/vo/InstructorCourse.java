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
public class InstructorCourse {
    private Long id;
    private String instructorId;      // 강의자 ID
    private String courseName;        // 강의명
    private String description;       // 강의 설명
    private String category;          // 카테고리
    private String thumbnailUrl;      // 썸네일 URL
    private String videoUrl;          // 강의 영상 URL
    private Integer totalWeeks;       // 총 주차
    private Integer totalHours;       // 총 강의 시간
    private String difficulty;        // 난이도 (BEGINNER, INTERMEDIATE, ADVANCED)
    private String status;            // 상태 (DRAFT, PUBLISHED, ARCHIVED)
    private LocalDateTime createdAt;  // 생성일
    private LocalDateTime updatedAt;  // 수정일
    
    // 통계 정보
    private Integer enrollmentCount;  // 수강생 수
    private Double averageRating;     // 평균 평점
    private Integer reviewCount;      // 리뷰 수
} 