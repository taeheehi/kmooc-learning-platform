package com.kopo.learning.student.myclassroom;

import com.kopo.learning.common.vo.Wishlist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WishlistMapper {
    
    // 사용자의 찜한 강의 목록 조회
    List<Wishlist> findByUserId(@Param("userId") String userId);
    
    // 찜한 강의 추가
    int save(Wishlist wishlist);
    
    // 찜한 강의 삭제
    int delete(@Param("id") Long id);
    
    // 특정 강의 찜 여부 확인
    Wishlist findByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") Long courseId);
    
    // 찜한 강의 개수 조회
    int countByUserId(@Param("userId") String userId);
} 