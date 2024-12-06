package com.example.handle.function;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class StratumSqlProvider {
    public String getStratumInfoByDynamicParams(Map<String, Object> params) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("stratums");
            if (params.get("stratum_id") != null) {
                WHERE("stratum_id LIKE CONCAT('%', #{stratum_id}, '%')");
            }
            if (params.get("stratum_name") != null) {
                WHERE("stratum_name LIKE CONCAT('%', #{stratum_name}, '%')");
            }
            if (params.get("stratum_len") != null) {
                WHERE("stratum_len = #{stratum_len}");
            }
            if (params.get("stratum_add") != null) {
                WHERE("stratum_add LIKE CONCAT('%', #{stratum_add}, '%')");
            }
            if (params.get("stratum_pro") != null) {
                WHERE("stratum_pro LIKE CONCAT('%', #{stratum_pro}, '%')");
            }
            if (params.get("integrity") != null) {
                WHERE("integrity = #{integrity}");
            }
        }}.toString();
        return sql;
    }
} 