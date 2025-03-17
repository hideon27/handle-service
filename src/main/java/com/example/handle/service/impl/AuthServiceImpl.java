package com.example.handle.service.impl;

import com.example.handle.mapper.AuthMapper;
import com.example.handle.model.Users;
import com.example.handle.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public Long getResultByNamePassword(String username, String password) {
        return authMapper.getResultByNamePassword(username, password);
    }

    @Override
    public int getResultByNamePasswordAdmin(String username, String password) {
        return authMapper.getResultByNamePasswordAdmin(username, password);
    }

    @Override
    public int insertUser(String username, String number, String password, String name,
                         String sex, String id, String email, String tel, String en) {
        return authMapper.insertUser(username, number, password, name, sex, id, email, tel, en);
    }

    @Override
    public void updateLastLoginTime(String uAccount) {
        authMapper.updateLastLoginTime(uAccount);
    }

    @Override
    public void updateAdminLastLoginTime(String aAccount) {
        authMapper.updateAdminLastLoginTime(aAccount);
    }

    @Override
    public Users getUserInfoByAccount(String name) {
        return authMapper.getUserByAccount(name);
    }

    @Override
    public List<Users> getUserInfoByName(String u_account) {
        return authMapper.getUserInfoByName(u_account);
    }

    @Override
    public List<Users> getUserInfoByDynamicParams(Map<String, Object> params) {
        return authMapper.getUserInfoByDynamicParams(params);
    }

    @Override
    public List<Users> getUserInfoByAccNum(String account, String num) {
        return authMapper.getUserInfoByAccNum(account, num);
    }

    @Override
    public void updateUserInfoByAccount(String id, String u_account,
                                      String u_num, String u_password, String u_name,
                                      String u_sex, String u_id, String u_email,
                                      String u_tel, String u_et_name) {
        authMapper.updateUserInfoByAccount(id, u_account, u_num,
                u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
    }

    @Override
    public void deleteUserInfoById(String id) {
        authMapper.deleteUserInfoById(id);
    }
} 