package com.example.handle.mapper;

import com.example.handle.dto.resultdata.Engineering_1;
import com.example.handle.dto.resultdata.ImageAndStratum;
import com.example.handle.dto.resultdata.Stratums_1;
import com.example.handle.function.ImageSqlProvider;
import com.example.handle.function.UserSqlProvider;
import com.example.handle.model.Image_info;
import com.example.handle.model.Users;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@Mapper
public interface HandleMapper {

    ////select

    //查找工程队
    @Results({
            @Result(property = "engineering_team_name", column = "engineering_team_name")
    })
    @Select("SELECT engineering_team_name FROM engineering")
    List<Engineering_1> getEngineeringTeamName();

    //从岩芯表获得岩芯信息
    @Results({
            @Result(property = "stratum_name", column = "stratum_name"),
            @Result(property = "stratum_id", column = "stratum_id")
    })
    @Select("SELECT stratum_name, stratum_id FROM stratums")
    List<Stratums_1> getStratumName();

    //通过姓名获取用户信息
    @Results({
            @Result(property = "u_account", column = "u_account"),
            @Result(property = "u_num", column = "u_num"),
            @Result(property = "u_password", column = "u_password"),
            @Result(property = "u_name", column = "u_name"),
            @Result(property = "u_sex", column = "u_sex"),
            @Result(property = "u_id", column = "u_id"),
            @Result(property = "u_email", column = "u_email"),
            @Result(property = "u_tel", column = "u_tel"),
            @Result(property = "u_et_name", column = "u_et_name"),
            @Result(property = "u_regdate", column = "u_regdate")
    })
    @Select("SELECT * FROM users WHERE u_account = #{account}")
    Users getUserInfoByAccount(@Param("account") String account)throws DataAccessException;

    //获得图片信息
    @Results({
            @Result(property = "image_id", column = "image_id"),
            @Result(property = "image_name", column = "image_name"),
            @Result(property = "image_path", column = "image_path"),
            @Result(property = "upload_time", column = "upload_time"),
            @Result(property = "uploader_num", column = "uploader_num"),
            @Result(property = "image_sid", column = "image_sid"),
            @Result(property = "ima_start", column = "ima_start"),
            @Result(property = "ima_end", column = "ima_end"),
            @Result(property = "s_type", column = "s_type"),
            @Result(property = "ima_depth", column = "ima_depth")
    })
    @Select("select * from image_info where image_name = #{image_name}")
    List<Image_info> getImageInfoByName(@Param("image_name") String image_name)throws DataAccessException;

    @Results({
            @Result(property = "u_account", column = "u_account"),
            @Result(property = "u_num", column = "u_num"),
            @Result(property = "u_password", column = "u_password"),
            @Result(property = "u_name", column = "u_name"),
            @Result(property = "u_sex", column = "u_sex"),
            @Result(property = "u_id", column = "u_id"),
            @Result(property = "u_email", column = "u_email"),
            @Result(property = "u_tel", column = "u_tel"),
            @Result(property = "u_et_name", column = "u_et_name"),
            @Result(property = "u_regdate", column = "u_regdate")
    })
    @Select("select * from users where u_account = #{u_account}")
    List<Users> getUserInfoByName(@Param("u_account") String u_account)throws DataAccessException;

    //通过账户和工号查询用户信息
    @Results({
            @Result(property = "u_account", column = "u_account"),
            @Result(property = "u_num", column = "u_num"),
            @Result(property = "u_password", column = "u_password"),
            @Result(property = "u_name", column = "u_name"),
            @Result(property = "u_sex", column = "u_sex"),
            @Result(property = "u_id", column = "u_id"),
            @Result(property = "u_email", column = "u_email"),
            @Result(property = "u_tel", column = "u_tel"),
            @Result(property = "u_et_name", column = "u_et_name"),
            @Result(property = "u_regdate", column = "u_regdate")
    })
    @Select("select * from users where u_account = #{account} AND u_num = #{num}")
    List<Users> getUserInfoByAccNum(@Param("account") String account,
                                    @Param("num") String num) throws DataAccessException;

    //通过姓名和密码获得匹配结果，1为通过，0为未通过
    @Select("SELECT compare_userpsw(#{u_account}, #{u_password}) AS result")
    int getResultByNamePassword(@Param("u_account") String u_account,
                                @Param("u_password") String u_password);

    @Select("SELECT compare_adminipsw(#{a_account}, #{a_password}) AS result")
    int getResultByNamePasswordAdmin(@Param("a_account") String a_account,
                                @Param("a_password") String a_password);

    //通过账户号查找工号
    @Select("SELECT u_num FROM users WHERE u_account = #{u_account}")
    String getIdByAccount(@Param("u_account") String u_account);

    //通过输入参数情况进行动态查询
    @Results({
            @Result(property = "image_name", column = "image_name"),
            @Result(property = "stratum_name", column = "stratum_name"),
            @Result(property = "ima_start", column = "ima_start"),
            @Result(property = "ima_end", column = "ima_end")
    })
    @SelectProvider(type = ImageSqlProvider.class, method = "getImageInfoByDynamicParams")
    List<ImageAndStratum> getImageInfoByDynamicParams(Map<String, Object> params)throws DataAccessException;



    @Results({
            @Result(property = "u_account", column = "u_account"),
            @Result(property = "u_num", column = "u_num"),
            @Result(property = "u_password", column = "u_password"),
            @Result(property = "u_name", column = "u_name"),
            @Result(property = "u_sex", column = "u_sex"),
            @Result(property = "u_id", column = "u_id"),
            @Result(property = "u_email", column = "u_email"),
            @Result(property = "u_tel", column = "u_tel"),
            @Result(property = "u_et_name", column = "u_et_name"),
            @Result(property = "u_regdate", column = "u_regdate")
    })
    @SelectProvider(type = UserSqlProvider.class, method = "getUserInfoByDynamicParams")
    List<Users> getUserInfoByDynamicParams(Map<String, Object> params)throws DataAccessException;



    ////insert

    //用户信息插入
    @Insert("INSERT INTO users (u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name) VALUES (#{u_account}, #{u_num}, #{u_password}, #{u_name}, #{u_sex}, #{u_id}, #{u_email}, #{u_tel}, #{u_et_name})")
    int insertUser(@Param("u_account") String u_account,
                   @Param("u_num") String u_num,
                   @Param("u_password") String u_password,
                   @Param("u_name") String u_name,
                   @Param("u_sex") String u_sex,
                   @Param("u_id") String u_id,
                   @Param("u_email") String u_email,
                   @Param("u_tel") String u_tel,
                   @Param("u_et_name") String u_et_name)throws DataAccessException;

    //图片信息插入
    @Insert("INSERT INTO image_info (image_name, image_path, uploader_num, image_sid, ima_start, ima_end, ima_depth, s_type) VALUES (#{image_name}, #{image_path}, #{image_num}, #{image_sid}, #{ima_start}, #{ima_end}, #{ima_depth}, #{s_type})")
    int insertImageInfo(@Param("image_name") String image_name,
                        @Param("image_path") String image_path,
                        @Param("image_num") String image_num,
                        @Param("image_sid") String image_sid,
                        @Param("ima_start") double ima_start,
                        @Param("ima_end") double ima_end,
                        @Param("ima_depth") double ima_depth,
                        @Param("s_type") String s_type)throws DataAccessException;


    ////update

    //通过名字更新图片信息
    @Update("update image_info set image_name = #{image_name1}, image_sid = #{image_sid}, ima_start = #{ima_start}, ima_end = #{ima_end}, ima_depth = #{ima_depth}, s_type = #{s_type} where image_name = #{image_name2}")
    void updateImageInfoByName(@Param("image_name1") String image_name1,
                @Param("image_sid") String image_sid,
                @Param("ima_start") double ima_start,
                @Param("ima_end") double ima_end,
                @Param("ima_depth") double ima_depth,
                @Param("s_type") String s_type,
                @Param("image_name2") String image_name2);

    @Update("update users set u_account = #{u_account}, u_num = #{u_num}, u_password = #{u_password}, u_name = #{u_name}, u_sex = #{u_sex}, u_id = #{u_id} ," +
            " u_email = #{u_email}, u_tel = #{u_tel}, u_et_name = #{u_et_name} where u_account = #{u_oldAccount}")
    void updateUserInfoByAccount(@Param("u_oldAccount") String u_oldAccount,
                                 @Param("u_account") String u_account,
                                 @Param("u_num") String u_num,
                                 @Param("u_password") String u_password,
                                 @Param("u_name") String u_name,
                                 @Param("u_sex") String u_sex,
                                 @Param("u_id") String u_id,
                                 @Param("u_email") String u_email,
                                 @Param("u_tel") String u_tel,
                                 @Param("u_et_name") String u_et_name);




    ////delete

    //通过图片名删除图片信息
    @Delete("delete from image_info where image_name = #{image_name}")
    void deleteImageInfoByImageName(@Param("image_name") String image_name);

    @Delete("delete from users where u_account = #{u_account}")
    void deleteUserInfoByAccount(@Param("u_account") String u_account);



}

