package com.example.handle.controller;

import com.example.handle.dto.ApiResponse;
import com.example.handle.dto.requestdata.AdminLoginDTO;
import com.example.handle.dto.requestdata.UserLoginDTO;
import com.example.handle.dto.requestdata.UserRegisterDTO;
import com.example.handle.function.JWTUtils;
import com.example.handle.model.Users;
import com.example.handle.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "认证接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthService authService;

    @ApiOperation("用户登录")
    @PostMapping("/post/login")
    public ApiResponse<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        Long userId = authService.getResultByNamePassword(username, password);
        if (userId != null) {
            // 更新最后登录时间
            authService.updateLastLoginTime(username);
            // 生成token
            Map<String, String> payload = new HashMap<>();
            payload.put("userId", username);
            String token = JWTUtils.getToken(payload);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", userId);  // 返回用户ID给前端
            return ApiResponse.success(response);
        } else {
            return ApiResponse.fail("账号出错");
        }
    }

    @ApiOperation("管理员登录")
    @PostMapping("/post/adminLogin")
    public ApiResponse<?> loginAdmin(@RequestBody AdminLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        int result = authService.getResultByNamePasswordAdmin(username, password);
        if (result == 1) {
            // 更新最后登录时间
            authService.updateAdminLastLoginTime(username);
            // 生成token
            Map<String, String> payload = new HashMap<>();
            payload.put("userId", username);
            String token = JWTUtils.getToken(payload);
            return ApiResponse.success(Collections.singletonMap("token", token));
        } else {
            return ApiResponse.fail("账号出错");
        }
    }

    @ApiOperation("注册用户")
    @PostMapping("/post/register")
    public ApiResponse<?> register(@RequestBody UserRegisterDTO registerDTO) {
        // 获取必填字段
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        // 设置可选字段的默认值
        String name = registerDTO.getName() != null ? registerDTO.getName() : "hide";
        String sex = registerDTO.getSex() != null ? registerDTO.getSex() : "男";
        String email = registerDTO.getEmail() != null ? registerDTO.getEmail() : "hide@hide.com";
        String tel = registerDTO.getTel() != null ? registerDTO.getTel() : "123";
        String en = registerDTO.getEn() != null ? registerDTO.getEn() : "第一工程队";
        String number = registerDTO.getNumber() != null ? registerDTO.getNumber() : "U1001";
        String id = registerDTO.getId() != null ? registerDTO.getId() : "111111111111111111";
        int result = 0;
        try {
            result = authService.insertUser(username, number, password, name, sex, id, email, tel, en);
        } catch (DataAccessException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return ApiResponse.fail("工号重复");
            }
        }
        if (result == 1) {
            return ApiResponse.success(Collections.singletonMap("result", "注册成功"));
        } else {
            return ApiResponse.fail("存在其他问题");
        }
    }

    @ApiOperation("用户信息token")
    @GetMapping("/userinfo")
    public ApiResponse<?> userinfo(HttpServletRequest request) {
        Users result;
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        com.auth0.jwt.interfaces.DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();
        try {
            result = authService.getUserInfoByAccount(account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("获得用户信息")
    @GetMapping("/change/showUserInfo")
    public ApiResponse<?> showUserInfo(@RequestParam String u_account) {
        List<Users> result;
        try {
            result = authService.getUserInfoByName(u_account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        // 返回用户信息的成功响应
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得用户信息")
    @GetMapping("/get/getUserInfo")
    public ApiResponse<?> getUserInfo(@RequestParam(required = false) String account,
                                    @RequestParam(required = false) String num,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String sex,
                                    @RequestParam(required = false) String tel,
                                    @RequestParam(required = false) String etName) {
        Map<String, Object> params = new HashMap<>();
        List<Users> result;
        params.put("u_account", account);
        params.put("u_num", num);
        params.put("u_name", name);
        params.put("u_sex", sex);
        params.put("u_tel", tel);
        params.put("u_et_name", etName);
        try {
            result = authService.getUserInfoByDynamicParams(params);
            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            return ApiResponse.success(response);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/change/updateSubmitUser") 
    public ApiResponse<?> updateSubmitUser(@RequestBody Map<String, String> receivedData) {
        String id = receivedData.get("id");
        if (id == null) {
            return ApiResponse.fail("id不能为空");
        }
        String u_account = receivedData.getOrDefault("uAccount", "");
        String u_num = receivedData.getOrDefault("uNum", "");
        String u_password = receivedData.getOrDefault("uPassword", "");
        String u_name = receivedData.getOrDefault("uName", "");
        String u_sex = receivedData.getOrDefault("uSex", "");
        String u_id = receivedData.getOrDefault("uId", "");
        String u_email = receivedData.getOrDefault("uEmail", "");
        String u_tel = receivedData.getOrDefault("uTel", "");
        String u_et_name = receivedData.getOrDefault("uEtName", "");
        try {
            authService.updateUserInfoByAccount(id, u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return ApiResponse.fail("更新失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("删除用户")
    @PostMapping("/change/deleteUser")
    public ApiResponse<?> deleteUser(@RequestBody Map<String, String> receivedData) {
        String id = receivedData.get("id");
        try {
            authService.deleteUserInfoById(id);
        } catch (DataAccessException e) {
            return ApiResponse.fail("删除失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }
} 