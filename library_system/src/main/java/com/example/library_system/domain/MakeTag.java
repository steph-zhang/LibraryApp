package com.example.library_system.domain;

import lombok.Data;

@Data
public class MakeTag {
    int subjectId;
    int tagId;

    public MakeTag(int subject_id,int tag_id){
        subjectId = subject_id;
        tagId = tag_id;
    }
}
