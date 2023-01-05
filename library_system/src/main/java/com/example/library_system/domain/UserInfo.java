package com.example.library_system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInfo {
    @TableId(value = "account")
    String account;
    String password;
    String role;
    String username;
    String gender;
    String phone;
}
