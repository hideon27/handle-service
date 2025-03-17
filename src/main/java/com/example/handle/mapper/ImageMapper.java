package com.example.handle.mapper;

import com.example.handle.model.CoreSegments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ImageMapper {
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

    List<CoreSegments> getImageInfoByIdAndName(@Param("imageId") String imageId,
                                             @Param("imageName") String imageName);

    @Select("SELECT u_num FROM users WHERE u_account = #{u_account}")
    String getIdByAccount(@Param("u_account") String u_account);

    void updateImageInfoByName(@Param("imageName") String imageName,
                             @Param("stratumId") String stratumId,
                             @Param("segStart") double segStart,
                             @Param("segEnd") double segEnd,
                             @Param("segLen") double segLen,
                             @Param("segType") String segType,
                             @Param("imageId") String imageId);

    void deleteImageInfoByImageId(@Param("image_id") String image_id);
} 