package com.example.library_system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BookInfo {
    @TableId(value = "bookId")
    String bookId;
    String title;
    int rest;
    String author;
    int total;
    String tag;
}
