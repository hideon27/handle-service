package com.example.handle.function;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class ImageSqlProvider {
    public String getImageInfoByDynamicParams(Map<String, Object> params) {
        return new SQL() {{
            SELECT("i.image_name, s.stratum_name, i.seg_start, i.seg_end");
            FROM("core_segments i");
            LEFT_OUTER_JOIN("stratums s ON i.stratum_id = s.stratum_id");
            
            if (params.get("image_name") != null) {
                WHERE("i.image_name LIKE CONCAT('%', #{image_name}, '%')");
            }
            if (params.get("stratum_id") != null) {
                WHERE("i.stratum_id = #{stratum_id}");
            }
            if (params.get("seg_start") != null) {
                WHERE("i.seg_start > #{seg_start}");
            }
            if (params.get("seg_end") != null) {
                WHERE("i.seg_end < #{seg_end}");
            }
            if (params.get("seg_len") != null) {
                WHERE("i.seg_len = #{seg_len}");
            }
            if (params.get("seg_type") != null) {
                WHERE("i.seg_type LIKE CONCAT('%', #{seg_type}, '%')");
            }
            if (params.get("uploader_num") != null) {
                WHERE("i.uploader_num = #{uploader_num}");
            }
        }}.toString();
    }
}