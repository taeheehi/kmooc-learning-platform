package com.kopo.learning.community.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    
    private Long idx;
    private String boardType; // NOTICE, FREE, QNA, REVIEW
    private String title;
    private String content;
    private String authorId;
    private String authorDisplay; // 화면 표시용 작성자 (역할 + 이름)
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 