package com.kopo.learning.community.vo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    
    // 게시판 타입별 게시글 목록 조회 (최신순)
    List<Post> selectPostsByBoardType(@Param("boardType") String boardType);
    
    // 게시글 상세 조회
    Post selectPostById(@Param("idx") Long idx);
    
    // 게시글 등록
    int insertPost(Post post);
    
    // 게시글 수정
    int updatePost(Post post);
    
    // 게시글 삭제
    int deletePost(@Param("idx") Long idx);
    
    // 조회수 증가
    int increaseViewCount(@Param("idx") Long idx);
} 