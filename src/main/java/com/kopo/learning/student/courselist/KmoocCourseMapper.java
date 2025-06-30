package com.kopo.learning.student.courselist;

import com.kopo.learning.common.vo.KmoocCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface KmoocCourseMapper {
    List<KmoocCourse> findByCourseNameContainingIgnoreCaseAndCategory(@Param("keyword") String keyword, @Param("category") String category);
    List<KmoocCourse> findByCourseNameContainingIgnoreCase(@Param("keyword") String keyword);
    List<KmoocCourse> findByCategory(@Param("category") String category);
    List<KmoocCourse> findByCategoryIn(@Param("categories") List<String> categories);
    List<KmoocCourse> findByCourseNameContainingIgnoreCaseAndCategoryIn(@Param("keyword") String keyword, @Param("categories") List<String> categories);
    long countByCategory(@Param("category") String category);
    List<String> findDistinctCategories();
    List<KmoocCourse> findAll();
    int save(KmoocCourse course);
    int update(KmoocCourse course);
} 