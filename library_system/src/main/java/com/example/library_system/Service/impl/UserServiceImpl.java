package com.example.library_system.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.library_system.Dao.UserDao;
import com.example.library_system.Service.UserService;
import com.example.library_system.domain.UserInfo;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean register(UserInfo user) {
        return userDao.insert(user) > 0;
    }

    @Override
    public boolean update(String account,String username,String phone,String originPassword,String newPassword) {
        UserInfo user = new UserInfo();
        user.setAccount(account);
        user.setPhone(phone);
        user.setUsername(username);
        if(originPassword == null)
            user.setPassword(userDao.selectById(user.getAccount()).getPassword());
        else{
            String pswd = userDao.selectById(user.getAccount()).getPassword();
            if(!originPassword.equals(pswd)) {
                return false;
            }
            else{
                user.setPassword(newPassword);
            }
        }
        if(user.getGender() == null)
            user.setGender(userDao.selectById(user.getAccount()).getGender());
        if(user.getPhone() == null)
            user.setPhone(userDao.selectById(user.getAccount()).getPhone());
        if(user.getRole() == null)
            user.setRole(userDao.selectById(user.getAccount()).getRole());

        return userDao.updateById(user) > 0;
    }

    @Override
    public boolean delete(String account) {
        return userDao.deleteById(account) > 0;
    }

    @Override
    public UserInfo getById(String account) {
        return userDao.selectById(account);
    }

    @Override
    public List<UserInfo> getAll() {
        return userDao.selectList(null);
    }

    @Override
    public List<UserInfo> login(UserInfo userInfo) {
        System.out.println(userInfo);
        HashMap<String,Object> map = new HashMap<>();
        map.put("account",userInfo.getAccount());
        map.put("password",userInfo.getPassword());
//        System.out.println(userDao.selectByMap(map));
        return userDao.selectByMap(map);
    }
}
