package com.example.library_system.controller;

import com.example.library_system.Service.UserService;
import com.example.library_system.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public boolean register(UserInfo userInfo){
        return userService.register(userInfo);
    }

    @PostMapping(value = "/login")
    public UserInfo login(UserInfo userInfo){
        List<UserInfo> list = userService.login(userInfo);
        System.out.println(list);
        if(list.size() == 1){
            return list.get(0);
        }
        return null;
    }

    @PostMapping("/update")
    public boolean getById(String account,String username,String phone,String originPassword,String newPassword){
        return userService.update(account,username,phone,originPassword,newPassword);
    }

    @PostMapping(value = "/getAll")
    public List<UserInfo> getAll(){
        return userService.getAll();
    }
}
