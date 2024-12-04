package com.example.handle.function;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class ImageSqlProvider {
    public String getImageInfoByDynamicParams(Map<String, Object> params) {
        return new SQL() {{
            SELECT("i.image_name, s.stratum_name, i.ima_start, i.ima_end");
            FROM("core_segments i");
            LEFT_OUTER_JOIN("stratums s ON i.stratum_id = s.stratum_id");
            
            if (params.get("image_sid") != null) {
                WHERE("i.stratum_id LIKE CONCAT('%', #{image_sid}, '%')");
            }
            if (params.get("ima_start") != null) {
                WHERE("i.seg_start > #{ima_start}");
            }
            if (params.get("ima_end") != null) {
                WHERE("i.seg_end < #{ima_end}");
            }
            if (params.get("ima_depth") != null) {
                WHERE("i.seg_len = #{ima_depth}");
            }
            if (params.get("s_type") != null) {
                WHERE("i.seg_type LIKE CONCAT('%', #{s_type}, '%')");
            }
        }}.toString();
    }
}