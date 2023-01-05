package com.example.library_system.domain;

import lombok.Data;

@Data
public class SubjectInfo {
    Subject subject;
    String tags;
    public SubjectInfo(Subject s,String t){
        subject = s;
        tags = t;
    }
}
