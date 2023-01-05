package com.example.library_system.domain;

import lombok.Data;

@Data
public class Classify {
    String bookId;
    int subjectId;
    public Classify(String book_id,int subject_id){
        bookId = book_id;
        subjectId = subject_id;
    }
}
