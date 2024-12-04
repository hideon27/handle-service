package com.example.handle.function;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class UserSqlProvider {
    public String getUserInfoByDynamicParams(Map<String, Object> params) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("users");
            if (params.get("u_account") != null) {
                WHERE("u_account LIKE CONCAT('%', #{u_account}, '%')");
            }
            if (params.get("u_num") != null) {
                WHERE("u_num LIKE CONCAT('%', #{u_num}, '%')");
            }
            if (params.get("u_name") != null) {
                WHERE("u_name LIKE CONCAT('%', #{u_name}, '%')");
            }
            if (params.get("u_sex") != null) {
                WHERE("u_sex LIKE CONCAT('%', #{u_sex}, '%')");
            }
            if (params.get("u_tel") != null) {
                WHERE("u_tel LIKE CONCAT('%', #{u_tel}, '%')");
            }
            // 根据需要添加更多条件
        }}.toString();
        //System.out.println("Generated SQL: " + sql);
        return sql; 
    }
}