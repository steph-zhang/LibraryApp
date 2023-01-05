package com.example.library_system.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library_system.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserInfo> {
}
