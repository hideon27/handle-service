package com.example.handle.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ImageMapper {
    @Insert("INSERT INTO core_segments (image_id, image_name, image_path, uploader_num, stratum_id, stratum_len, seg_start, seg_end, seg_len, seg_type, sequence_no) " +
            "VALUES (#{imageId}, #{imageName}, #{imagePath}, #{uploaderNum}, #{stratumId}, #{stratumLen}, #{segStart}, #{segEnd}, #{segLen}, #{segType}, #{sequenceNo})")
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
                       @Param("sequenceNo") String sequenceNo);

    @Select("<script>" +
            "SELECT c.id AS id, c.image_id AS imageId, c.image_name AS imageName, c.image_path AS imagePath, " +
            "c.upload_time AS uploadTime, c.uploader_num AS uploaderNum, c.seg_start AS segStart, " +
            "c.seg_end AS segEnd, c.seg_len AS segLen, c.seg_type AS segType, c.sequence_no AS sequenceNo, " +
            "c.stratum_id AS stratumId, c.stratum_len AS stratumLen, c.edit_time AS editTime, " +
            "s.stratum_name AS stratumName " +
            "FROM core_segments c " +
            "LEFT JOIN stratums s ON c.stratum_id = s.stratum_id " +
            "<where>" +
            "<if test='imageId != null and imageId != \"\"'>AND c.image_id LIKE CONCAT('%', #{imageId}, '%')</if>" +
            "<if test='imageName != null and imageName != \"\"'>AND c.image_name LIKE CONCAT('%', #{imageName}, '%')</if>" +
            "<if test='segType != null and segType != \"\"'>AND c.seg_type LIKE CONCAT('%', #{segType}, '%')</if>" +
            "<if test='segLen != null and segLen != \"\"'>AND c.seg_len = #{segLen}</if>" +
            "<if test='segStart != null and segStart != \"\"'>AND c.seg_start &gt;= #{segStart}</if>" +
            "<if test='segEnd != null and segEnd != \"\"'>AND c.seg_end &lt;= #{segEnd}</if>" +
            "<if test='stratumId != null and stratumId != \"\"'>AND c.stratum_id = #{stratumId}</if>" +
            "<if test='uploaderNum != null and uploaderNum != \"\"'>AND c.uploader_num = #{uploaderNum}</if>" +
            "</where>" +
            "ORDER BY c.upload_time DESC" +
            "</script>")
    List<Map<String, Object>> getImageInfoByIdAndName(@Param("imageId") String imageId,
                                                      @Param("imageName") String imageName,
                                                      @Param("segType") String segType,
                                                      @Param("segLen") String segLen,
                                                      @Param("segStart") String segStart,
                                                      @Param("segEnd") String segEnd,
                                                      @Param("stratumId") String stratumId,
                                                      @Param("uploaderNum") String uploaderNum);

    @Select("SELECT u_num FROM users WHERE u_account = #{u_account}")
    String getIdByAccount(@Param("u_account") String u_account);

    @Select("SELECT uploader_num FROM core_segments WHERE image_id = #{imageId}")
    String getUploaderNumByImageId(@Param("imageId") String imageId);

    @Update("<script>" +
            "UPDATE core_segments " +
            "<set>" +
            "<if test='imageName != null and imageName != \"\"'>image_name = #{imageName},</if>" +
            "<if test='stratumId != null and stratumId != \"\"'>stratum_id = #{stratumId},</if>" +
            "<if test='segStart != -1'>seg_start = #{segStart},</if>" +
            "<if test='segEnd != -1'>seg_end = #{segEnd},</if>" +
            "<if test='segLen != -1'>seg_len = #{segLen},</if>" +
            "<if test='segType != null and segType != \"\"'>seg_type = #{segType},</if>" +
            "</set>" +
            "WHERE image_id = #{imageId}" +
            "</script>")
    void updateImageInfoByName(@Param("imageName") String imageName,
                             @Param("stratumId") String stratumId,
                             @Param("segStart") double segStart,
                             @Param("segEnd") double segEnd,
                             @Param("segLen") double segLen,
                             @Param("segType") String segType,
                             @Param("imageId") String imageId);

    @Delete("DELETE FROM core_segments WHERE image_id = #{image_id}")
    void deleteImageInfoByImageId(@Param("image_id") String image_id);
} 
