package io.toolongname.sandcastle.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class ActiveCodeMapperProvider {
    private static final String ACTIVE_CODE_TABLE_NAME = "sandcastle_active_code";

    public String addSql() {
        return new SQL() {{
            INSERT_INTO(ACTIVE_CODE_TABLE_NAME);
            VALUES("user_uuid", "#{userUuidByte}");
            VALUES("status", "#{status}");
            VALUES("code", "#{code}");
        }}.toString();
    }

    public String getByCodeSql() {
        return new SQL() {{
            SELECT("*");
            FROM(ACTIVE_CODE_TABLE_NAME);
            WHERE("code = #{code}");
        }}.toString();
    }

    public String listByStatusSql(){
        return new SQL(){{
            SELECT("*");
            FROM(ACTIVE_CODE_TABLE_NAME);
            WHERE("status = #{status}");
        }}.toString();
    }

    public String modifyByIdSql(){
        return new SQL(){{
            UPDATE(ACTIVE_CODE_TABLE_NAME);
            SET("user_uuid = #{userUuid}");
            SET("status = #{status}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
