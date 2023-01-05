package com.example.library_system.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library_system.domain.Borrow;
import com.example.library_system.domain.ReturnBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReturnBookDao extends BaseMapper<ReturnBook> {
}
