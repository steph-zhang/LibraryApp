package com.example.library_system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Tag {
    @TableId(value = "tagId")
    int tagId;
    String tagName;
    String description;
}
