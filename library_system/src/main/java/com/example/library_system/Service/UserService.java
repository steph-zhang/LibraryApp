package com.example.library_system.Service;

import com.example.library_system.domain.UserInfo;

import java.util.List;

public interface UserService {
    boolean register(UserInfo user);
    boolean update(String account,String username,String phone,String originPassword,String newPassword);
    boolean delete(String account);
    UserInfo getById(String account);
    List<UserInfo> getAll();
    List<UserInfo> login(UserInfo userInfo);
}
