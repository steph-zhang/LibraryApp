package com.example.library_system.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Borrow {
    String account;
    String bookId;
    Date date;
}
