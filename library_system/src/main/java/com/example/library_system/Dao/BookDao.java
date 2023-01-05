package com.example.library_system.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library_system.domain.BookInfo;
import com.example.library_system.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookDao extends BaseMapper<BookInfo> {
    List<UserInfo> queryUsersByTitle(@Param("title") String title);
}
