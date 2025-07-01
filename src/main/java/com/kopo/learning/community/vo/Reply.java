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
public class Reply {
    private Long id;
    private Long postId;
    private String userId; // 작성자 id
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 