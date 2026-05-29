package com.example.handle.mapper;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.dto.resultdata.StratumSegmentDTO;
import com.example.handle.function.ImageSqlProvider;
import com.example.handle.function.StratumSqlProvider;
import com.example.handle.model.Stratums;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface StratumMapper {
    @Insert("INSERT INTO stratums (stratum_id, stratum_name, stratum_len, stratum_add, stratum_pro) " +
            "VALUES (#{stratumId}, #{stratumName}, #{stratumLen}, #{stratumAdd}, #{stratumPro})")
    int insertStratumInfo(@Param("stratumId") String stratumId,
                         @Param("stratumName") String stratumName,
                         @Param("stratumLen") double stratumLen,
                         @Param("stratumAdd") String stratumAdd,
                         @Param("stratumPro") String stratumPro);

    @Select("SELECT stratum_len FROM stratums WHERE stratum_id = #{stratumId}")
    Double getStratumLength(@Param("stratumId") String stratumId);

    @Select("SELECT COALESCE(SUM(seg_len), 0) FROM core_segments WHERE stratum_id = #{stratumId}")
    Double getTotalSegmentLength(@Param("stratumId") String stratumId);

    @Select("SELECT s.stratum_id AS stratumId, s.stratum_name AS stratumName, s.stratum_len AS stratumLen, s.stratum_add AS stratumAdd, " +
            "c.image_id AS imageId, c.image_name AS imageName, c.image_path AS imagePath, c.seg_start AS segStart, c.seg_end AS segEnd, " +
            "c.seg_len AS segLen, c.seg_type AS segType, c.sequence_no AS sequenceNo " +
            "FROM stratums s LEFT JOIN core_segments c ON s.stratum_id = c.stratum_id " +
            "WHERE s.stratum_id = #{stratumId}")
    List<StratumSegmentDTO> getStratumAndSegments(@Param("stratumId") String stratumId);

    @Update("UPDATE core_segments SET sequence_no = #{sequenceNo} WHERE stratum_id = #{stratumId} AND seg_start = #{segStart}")
    void updateSequenceNo(@Param("stratumId") String stratumId,
                         @Param("segStart") double segStart,
                         @Param("sequenceNo") int sequenceNo);

    @Update("UPDATE stratums SET integrity = #{integrity} WHERE stratum_id = #{stratumId}")
    void updateStratumIntegrity(@Param("stratumId") String stratumId,
                               @Param("integrity") String integrity);

    @Select("SELECT engineering_team_name AS engineeringTeamName FROM engineering")
    List<EngineeringDTO> getEngineeringTeamName();

    @Select("SELECT stratum_name AS stratumName, stratum_id AS stratumId, stratum_len AS stratumLen FROM stratums")
    List<StratumDTO> getStratumName();

    @SelectProvider(type = ImageSqlProvider.class, method = "getImageInfoByDynamicParams")
    @Results({
            @Result(property = "imageId", column = "image_id"),
            @Result(property = "imageName", column = "image_name"),
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "segStart", column = "seg_start"),
            @Result(property = "segEnd", column = "seg_end")
    })
    List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params);

    @Select("SELECT id, stratum_id AS stratumId, stratum_name AS stratumName, stratum_len AS stratumLen, " +
            "stratum_add AS stratumAdd, stratum_pro AS stratumPro, integrity, create_time AS createTime " +
            "FROM stratums WHERE stratum_id LIKE CONCAT('%', #{stratum_id}, '%')")
    List<Stratums> getStratumInfoByName(@Param("stratum_id") String stratum_id);

    @SelectProvider(type = StratumSqlProvider.class, method = "getStratumInfoByDynamicParams")
    @Results(id = "stratumResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "stratumId", column = "stratum_id"),
            @Result(property = "stratumName", column = "stratum_name"),
            @Result(property = "stratumLen", column = "stratum_len"),
            @Result(property = "stratumAdd", column = "stratum_add"),
            @Result(property = "stratumPro", column = "stratum_pro"),
            @Result(property = "integrity", column = "integrity"),
            @Result(property = "createTime", column = "create_time")
    })
    List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params);

    @Update("<script>" +
            "UPDATE stratums " +
            "<set>" +
            "<if test='stratumName != null and stratumName != \"\"'>stratum_name = #{stratumName},</if>" +
            "<if test='stratumLen != -1'>stratum_len = #{stratumLen},</if>" +
            "<if test='stratumAdd != null and stratumAdd != \"\"'>stratum_add = #{stratumAdd},</if>" +
            "<if test='stratumPro != null and stratumPro != \"\"'>stratum_pro = #{stratumPro},</if>" +
            "<if test='integrity != null and integrity != \"\"'>integrity = #{integrity},</if>" +
            "</set>" +
            "WHERE stratum_id = #{stratumId}" +
            "</script>")
    void updateStratumInfoById(@Param("stratumId") String stratumId,
                              @Param("stratumName") String stratumName,
                              @Param("stratumLen") double stratumLen,
                              @Param("stratumAdd") String stratumAdd,
                              @Param("stratumPro") String stratumPro,
                              @Param("integrity") String integrity);

    @Delete("DELETE FROM stratums WHERE stratum_id = #{stratum_id}")
    void deleteStratumInfoById(@Param("stratum_id") String stratum_id);

    @Select("SELECT stratum_id FROM stratums")
    List<Map<String, Object>> getAllStrataWithIssues();

    @Select("SELECT id, stratum_id AS stratumId, stratum_name AS stratumName, stratum_len AS stratumLen, " +
            "stratum_add AS stratumAdd, stratum_pro AS stratumPro, integrity, create_time AS createTime FROM stratums")
    List<Stratums> getAllStratums();   
} 
