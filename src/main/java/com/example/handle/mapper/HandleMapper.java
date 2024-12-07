package com.example.handle.mapper;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.function.ImageSqlProvider;
import com.example.handle.function.UserSqlProvider;
import com.example.handle.function.StratumSqlProvider;
import com.example.handle.model.CoreSegments;
import com.example.handle.model.Users;
import com.example.handle.model.Administrators;
import com.example.handle.model.Stratums;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@Mapper
public interface HandleMapper {

    //// 1. 用户认证相关方法
    @Select("SELECT COUNT(*) FROM users WHERE u_account = #{u_account} AND u_password = #{u_password}")
    int getResultByNamePassword(@Param("u_account") String u_account, 
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

    //// 2. 图片上传和处理相关方法
    @Select("SELECT u_num FROM users WHERE u_account = #{u_account}")
    String getIdByAccount(@Param("u_account") String u_account);

    @Insert("INSERT INTO core_segments (image_id, image_name, image_path, uploader_num, stratum_id, stratum_len, " +
            "seg_start, seg_end, seg_len, seg_type, sequence_no) " +
            "VALUES (#{imageId}, #{imageName}, #{imagePath}, #{uploaderNum}, #{stratumId}, #{stratumLen}, " +
            "#{segStart}, #{segEnd}, #{segLen}, #{segType}, #{sequenceNo})")
    int insertImageInfo(@Param("imageId") String imageId,
                        @Param("imageName") String imageName,
                        @Param("imagePath") String imagePath,
                        @Param("uploaderNum") String uploaderNum,
                        @Param("stratumId") String stratumId,
                        @Param("stratumLen") double stratumLen,
                        @Param("segStart") double segStart,
                        @Param("segEnd") double segEnd,
                        @Param("segLen") double segLen,
                        @Param("segType") String segType,
                        @Param("sequenceNo") String sequenceNo) throws DataAccessException;

    @Insert("INSERT INTO stratums (stratum_id, stratum_name, stratum_len, stratum_add, stratum_pro, integrity) " +
            "VALUES (#{stratum_id}, #{stratum_name}, #{stratum_len}, #{stratum_add}, #{stratum_pro}, 'NO')")
    int insertStratumInfo(@Param("stratum_id") String stratum_id,
                         @Param("stratum_name") String stratum_name,
                         @Param("stratum_len") double stratum_len,
                         @Param("stratum_add") String stratum_add,
                         @Param("stratum_pro") String stratum_pro) throws DataAccessException;


    @Select("SELECT s.stratum_id, s.stratum_name, s.stratum_len, s.stratum_add, cs.image_id, cs.image_path, cs.seg_start, cs.seg_end, cs.seg_len, cs.seg_type, cs.sequence_no FROM stratums s LEFT JOIN core_segments cs ON s.stratum_id = cs.stratum_id WHERE s.stratum_id = #{stratumId}")
    List<Map<String, Object>> getStratumAndSegments(String stratumId);

    @Select("SELECT SUM(seg_len) FROM core_segments WHERE stratum_id = #{stratumId}")
    Double getTotalSegmentLength(String stratumId);

    @Select("SELECT stratum_len FROM stratums WHERE stratum_id = #{stratumId}")
    Double getStratumLength(String stratumId);

    @Update("UPDATE core_segments " +
            "SET sequence_no = #{sequenceNo} " +
            "WHERE stratum_id = #{stratumId} AND seg_start = #{segStart}")
    void updateSequenceNo(@Param("stratumId") String stratumId,
                         @Param("segStart") Double segStart,
                         @Param("sequenceNo") int sequenceNo);


    //// 3. 基础数据查询方法
    @Results({
            @Result(property = "engineeringTeamName", column = "engineering_team_name")
    })
    @Select("SELECT engineering_team_name FROM engineering")
    List<EngineeringDTO> getEngineeringTeamName();

    @Results({
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "stratumId", column = "stratum_id"),
            @Result(property = "stratumLen", column = "stratum_len")
    })
    @Select("SELECT stratum_name, stratum_id, stratum_len FROM stratums")
    List<StratumDTO> getStratumName();

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "imageId", column = "image_id"),
            @Result(property = "imageName", column = "image_name"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "uploadTime", column = "upload_time"),
            @Result(property = "uploaderNum", column = "uploader_num"),
            @Result(property = "segStart", column = "seg_start"),
            @Result(property = "segEnd", column = "seg_end"),
            @Result(property = "segLen", column = "seg_len"),
            @Result(property = "segType", column = "seg_type"),
            @Result(property = "sequenceNo", column = "sequence_no"),
            @Result(property = "stratumId", column = "stratum_id"),
            @Result(property = "stratumLen", column = "stratum_len"),
            @Result(property = "editTime", column = "edit_time")
    })
    @Select("SELECT * FROM core_segments WHERE image_name = #{image_name}")
    List<CoreSegments> getImageInfoByName(@Param("image_name") String image_name) throws DataAccessException;

    @Results({
            @Result(property = "imageName", column = "image_name"),
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "segStart", column = "seg_start"),
            @Result(property = "segEnd", column = "seg_end")
    })
    @SelectProvider(type = ImageSqlProvider.class, method = "getImageInfoByDynamicParams")
    List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params);

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
    List<Users> getUserInfoByName(@Param("u_account") String u_account) throws DataAccessException;

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

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "stratumId", column = "stratum_id"),
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "stratumLen", column = "stratum_len"),
            @Result(property = "stratumAdd", column = "stratum_add"),
            @Result(property = "stratumPro", column = "stratum_pro"),
            @Result(property = "integrity", column = "integrity"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT * FROM stratums WHERE stratum_id = #{stratum_id}")
    List<Stratums> getStratumInfoByName(@Param("stratum_id") String stratum_id) throws DataAccessException;

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "stratumId", column = "stratum_id"),
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "stratumLen", column = "stratum_len"),
            @Result(property = "stratumAdd", column = "stratum_add"),
            @Result(property = "stratumPro", column = "stratum_pro"),
            @Result(property = "integrity", column = "integrity"),
            @Result(property = "createTime", column = "create_time")
    })
    @SelectProvider(type = StratumSqlProvider.class, method = "getStratumInfoByDynamicParams")
    List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params) throws DataAccessException;

    //// 4. 信息管理相关方法
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

    @Update("UPDATE core_segments SET " +
            "image_name = #{imageName}, " +
            "stratum_id = #{stratumId}, " +
            "seg_start = #{segStart}, " +
            "seg_end = #{segEnd}, " +
            "seg_len = #{segLen}, " +
            "seg_type = #{segType} " +
            "WHERE image_name = #{oldImageName}")
    void updateImageInfoByName(@Param("imageName") String imageName,
                              @Param("stratumId") String stratumId,
                              @Param("segStart") double segStart,
                              @Param("segEnd") double segEnd,
                              @Param("segLen") double segLen,
                              @Param("segType") String segType,
                              @Param("oldImageName") String oldImageName);

    @Update("update users set u_account = #{u_account}, u_num = #{u_num}, u_password = #{u_password}, " +
            "u_name = #{u_name}, u_sex = #{u_sex}, u_id = #{u_id}, u_email = #{u_email}, " +
            "u_tel = #{u_tel}, u_et_name = #{u_et_name} where u_account = #{u_oldAccount}")
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

    @Update("UPDATE stratums SET " +
            "stratum_id = #{stratum_id}, " +
            "stratum_name = #{stratum_name}, " +
            "stratum_len = #{stratum_len}, " +
            "stratum_add = #{stratum_add}, " +
            "stratum_pro = #{stratum_pro}, " +
            "integrity = #{integrity} " +
            "WHERE stratum_id = #{old_stratum_id}")
    void updateStratumInfoById(@Param("old_stratum_id") String old_stratum_id,
                              @Param("stratum_id") String stratum_id,
                              @Param("stratum_name") String stratum_name,
                              @Param("stratum_len") double stratum_len,
                              @Param("stratum_add") String stratum_add,
                              @Param("stratum_pro") String stratum_pro,
                              @Param("integrity") String integrity) throws DataAccessException;

    @Delete("delete from core_segments where image_name = #{image_name}")
    void deleteImageInfoByImageName(@Param("image_name") String image_name);

    @Delete("delete from users where u_account = #{u_account}")
    void deleteUserInfoByAccount(@Param("u_account") String u_account);

    @Delete("delete from stratums where stratum_id = #{stratum_id}")
    void deleteStratumInfoById(@Param("stratum_id") String stratum_id) throws DataAccessException;
}

