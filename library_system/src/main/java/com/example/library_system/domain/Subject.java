package com.example.library_system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Subject {
    @TableId(value = "subjectId")
    int subjectId;
    String subjectName;
}
