package com.example.handle.mapper;

import com.example.handle.function.UserSqlProvider;
import com.example.handle.model.Administrators;
import com.example.handle.model.Users;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuthMapper {
    //// 用户认证相关方法
    @Select("SELECT id FROM users WHERE u_account = #{u_account} AND u_password = #{u_password}")
    Long getResultByNamePassword(@Param("u_account") String u_account, 
                               @Param("u_password") String u_password);

    @Select("SELECT COUNT(*) FROM administrators WHERE a_account = #{a_account} AND a_password = #{a_password}")
    int getResultByNamePasswordAdmin(@Param("a_account") String a_account, 
                                    @Param("a_password") String a_password);

    @Insert("INSERT INTO users (u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name) " +
            "VALUES (#{u_account}, #{u_num}, #{u_password}, #{u_name}, #{u_sex}, #{u_id}, #{u_email}, #{u_tel}, #{u_et_name})")
    int insertUser(@Param("u_account") String u_account,
                   @Param("u_num") String u_num,
                   @Param("u_password") String u_password,
                   @Param("u_name") String u_name,
                   @Param("u_sex") String u_sex,
                   @Param("u_id") String u_id,
                   @Param("u_email") String u_email,
                   @Param("u_tel") String u_tel,
                   @Param("u_et_name") String u_et_name) throws DataAccessException;

    @Update("UPDATE users SET last_login_time = CURRENT_TIMESTAMP WHERE u_account = #{uAccount}")
    void updateLastLoginTime(@Param("uAccount") String uAccount);
    

    @Update("UPDATE administrators SET last_login_time = CURRENT_TIMESTAMP WHERE a_account = #{aAccount}")
    void updateAdminLastLoginTime(@Param("aAccount") String aAccount);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uAccount", column = "u_account"),
            @Result(property = "uNum", column = "u_num"),
            @Result(property = "uPassword", column = "u_password"),
            @Result(property = "uName", column = "u_name"),
            @Result(property = "uSex", column = "u_sex"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "uEmail", column = "u_email"),
            @Result(property = "uTel", column = "u_tel"),
            @Result(property = "uEtName", column = "u_et_name"),
            @Result(property = "uRegdate", column = "u_regdate"),
            @Result(property = "lastLoginTime", column = "last_login_time")
    })
    @Select("SELECT * FROM users WHERE u_account = #{u_account}")
    Users getUserByAccount(@Param("u_account") String u_account);

    @Select("SELECT * FROM administrators WHERE a_account = #{a_account}")
    Administrators getAdminByAccount(@Param("a_account") String a_account);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uAccount", column = "u_account"),
            @Result(property = "uNum", column = "u_num"),
            @Result(property = "uPassword", column = "u_password"),
            @Result(property = "uName", column = "u_name"),
            @Result(property = "uSex", column = "u_sex"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "uEmail", column = "u_email"),
            @Result(property = "uTel", column = "u_tel"),
            @Result(property = "uEtName", column = "u_et_name"),
            @Result(property = "uRegdate", column = "u_regdate"),
            @Result(property = "lastLoginTime", column = "last_login_time")
    })
    @Select("SELECT * FROM users WHERE u_account = #{account} AND u_num = #{num}")
    List<Users> getUserInfoByAccNum(@Param("account") String account,
                                   @Param("num") String num) throws DataAccessException;

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uAccount", column = "u_account"),
            @Result(property = "uNum", column = "u_num"),
            @Result(property = "uPassword", column = "u_password"),
            @Result(property = "uName", column = "u_name"),
            @Result(property = "uSex", column = "u_sex"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "uEmail", column = "u_email"),
            @Result(property = "uTel", column = "u_tel"),
            @Result(property = "uEtName", column = "u_et_name"),
            @Result(property = "uRegdate", column = "u_regdate"),
            @Result(property = "lastLoginTime", column = "last_login_time")
    })
    @Select("SELECT * FROM users WHERE u_account LIKE CONCAT('%', #{u_account}, '%')")
    List<Users> getUserInfoByName(@Param("u_account") String u_account);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uAccount", column = "u_account"),
            @Result(property = "uNum", column = "u_num"),
            @Result(property = "uPassword", column = "u_password"),
            @Result(property = "uName", column = "u_name"),
            @Result(property = "uSex", column = "u_sex"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "uEmail", column = "u_email"),
            @Result(property = "uTel", column = "u_tel"),
            @Result(property = "uEtName", column = "u_et_name"),
            @Result(property = "uRegdate", column = "u_regdate"),
            @Result(property = "lastLoginTime", column = "last_login_time")
    })
    @SelectProvider(type = UserSqlProvider.class, method = "getUserInfoByDynamicParams")
    List<Users> getUserInfoByDynamicParams(Map<String, Object> params) throws DataAccessException;

    @Update("<script>" +
            "UPDATE users" +
            "<set>" +
            "<if test='u_account != \"\"'>u_account = #{u_account},</if>" +
            "<if test='u_num != \"\"'>u_num = #{u_num},</if>" +
            "<if test='u_password != \"\"'>u_password = #{u_password},</if>" +
            "<if test='u_name != \"\"'>u_name = #{u_name},</if>" +
            "<if test='u_sex != \"\"'>u_sex = #{u_sex},</if>" +
            "<if test='u_id != \"\"'>u_id = #{u_id},</if>" +
            "<if test='u_email != \"\"'>u_email = #{u_email},</if>" +
            "<if test='u_tel != \"\"'>u_tel = #{u_tel},</if>" +
            "<if test='u_et_name != \"\"'>u_et_name = #{u_et_name}</if>" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    void updateUserInfoByAccount(@Param("id") String id,
                                @Param("u_account") String u_account,
                                @Param("u_num") String u_num,
                                @Param("u_password") String u_password,
                                @Param("u_name") String u_name,
                                @Param("u_sex") String u_sex,
                                @Param("u_id") String u_id,
                                @Param("u_email") String u_email,
                                @Param("u_tel") String u_tel,
                                @Param("u_et_name") String u_et_name);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUserInfoById(@Param("id") String id);
} 