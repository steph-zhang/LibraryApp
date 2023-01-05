package com.example.library_system.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library_system.domain.Subject;
import com.example.library_system.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubjectDao extends BaseMapper<Subject> {
    List<String> querySubjectTags(@Param("subject_name")String subject_name);
}
