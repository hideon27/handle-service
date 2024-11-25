package com.example.handle.function;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class ImageSqlProvider {
    public String getImageInfoByDynamicParams(Map<String, Object> params) {
        String sql = new SQL() {{
            SELECT("i.image_name, s.stratum_name, i.ima_start, i.ima_end");
            FROM("image_info i");
            LEFT_OUTER_JOIN("stratums s ON i.image_sid = s.stratum_id");
            if (params.get("image_sid") != null) {
                WHERE("image_sid = #{image_sid}");
            }
            if (params.get("ima_start") != null) {
                WHERE("ima_start > #{ima_start}");
            }
            if (params.get("ima_end") != null) {
                WHERE("ima_end < #{ima_end}");
            }
            if (params.get("ima_depth") != null) {
                WHERE("ima_depth = #{ima_depth}");
            }
            if (params.get("s_type") != null) {
                WHERE("s_type = #{s_type}");
            }

            // 根据需要添加更多条件
        }}.toString();
        //System.out.println("Generated SQL: " + sql);
        return sql; 
    }
}