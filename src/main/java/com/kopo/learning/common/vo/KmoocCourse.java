package com.kopo.learning.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "kmooc_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KmoocCourse {

    @Id
    private Long id;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "category")
    private String category;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name = "url")
    private String url;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "name")
    private String name;

    @Column(name = "professor", columnDefinition = "TEXT")
    private String professor;

    @Column(name = "course_week")
    private String courseWeek;

    @Column(name = "study_start")
    private String studyStart;

    @Column(name = "study_end")
    private String studyEnd;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "site")
    private String site;
} 