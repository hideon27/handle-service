package com.example.handle.service;

import com.example.handle.model.Users;
// import com.example.handle.dto.ApiResponse;
import java.util.List;
import java.util.Map;

public interface AuthService {
    Long getResultByNamePassword(String username, String password);
    int getResultByNamePasswordAdmin(String username, String password);
    int insertUser(String username, String number, String password, String name, String sex, String id, String email, String tel, String en);
    void updateLastLoginTime(String uAccount);
    void updateAdminLastLoginTime(String aAccount);
    Users getUserInfoByAccount(String name);
    List<Users> getUserInfoByName(String u_account);
    List<Users> getUserInfoByDynamicParams(Map<String, Object> params);
    List<Users> getUserInfoByAccNum(String account, String num);
    void updateUserInfoByAccount(String id, String u_account, String u_num, String u_password, String u_name, String u_sex, String u_id, String u_email, String u_tel, String u_et_name);
    void deleteUserInfoById(String id);
} 