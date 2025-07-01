package com.kopo.learning.community.vo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReplyMapper {
    // 게시글별 댓글 목록
    List<Reply> selectRepliesByPostId(@Param("postId") Long postId);
    // 게시글별 댓글 목록 (페이징)
    List<Reply> selectRepliesByPostIdPaged(@Param("postId") Long postId, @Param("offset") int offset, @Param("limit") int limit);
    // 게시글별 전체 댓글 수
    int countRepliesByPostId(@Param("postId") Long postId);
    // 댓글 등록
    int insertReply(Reply reply);
    // 댓글 수정
    int updateReply(Reply reply);
    // 댓글 삭제
    int deleteReply(@Param("id") Long id);
    // 단일 댓글 조회
    Reply selectReplyById(@Param("id") Long id);
} 