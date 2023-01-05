package com.example.library_system.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class BorrowBookInfo {
    String bookId;
    String title;
    int rest;
    String author;
    int total;
    String tag;
    Date date;
    public BorrowBookInfo(BookInfo bookInfo, Date d){
        bookId = bookInfo.getBookId();
        title = bookInfo.getTitle();
        rest = bookInfo.getRest();
        author = bookInfo.getAuthor();
        total = bookInfo.getTotal();
        tag = bookInfo.getTag();
        date = d;
    }
}
