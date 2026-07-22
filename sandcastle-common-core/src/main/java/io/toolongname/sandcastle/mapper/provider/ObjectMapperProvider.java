package io.toolongname.sandcastle.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class ObjectMapperProvider {
    private static final String SANDCASTLE_OBJECT_TABLE_NAME = "sandcastle_object";

    public String addSqlBuilder() {
        return new SQL() {{
            INSERT_INTO(SANDCASTLE_OBJECT_TABLE_NAME);
            VALUES("uuid", "#{uuid}");
            VALUES("status", "#{status}");
            VALUES("sha512", "#{sha512}");
            VALUES("size", "#{size}");
            VALUES("path", "#{path}");
            VALUES("encrypt_key", "#{encryptKey}");
        }}.toString();
    }

    public String queryByUuidSqlBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_OBJECT_TABLE_NAME);
            WHERE("uuid = #{uuid}");
        }}.toString();
    }

    public String queryBySha512SqlBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_OBJECT_TABLE_NAME);
            WHERE("sha512 = #{sha512}");
        }}.toString();
    }
}
