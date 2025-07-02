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
public class Wishlist {
    private Long id;
    private String userId;        // 사용자 ID
    private Long courseId;        // 강의 ID
    private LocalDateTime addedAt; // 찜한 날짜
    
    // 조인을 위한 추가 정보
    private String courseName;
    private String teacherName;
    private String thumbnailUrl;
    private String category;
} 