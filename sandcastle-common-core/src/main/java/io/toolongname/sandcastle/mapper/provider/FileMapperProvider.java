package io.toolongname.sandcastle.mapper.provider;

import org.apache.ibatis.jdbc.SQL;
import org.jspecify.annotations.Nullable;

public class FileMapperProvider {

    private static final String SANDCASTLE_FILE_TABLE_NAME = "sandcastle_file";

    public String addSqlBuilder() {
        return new SQL() {{
            INSERT_INTO(SANDCASTLE_FILE_TABLE_NAME);
            VALUES("uuid", "#{uuid}");
            VALUES("user_uuid", "#{userUuid}");
            VALUES("object_uuid", "#{objectUuid}");
            VALUES("status", "#{status}");
            VALUES("flag", "#{flag}");
            VALUES("title", "#{title}");
            VALUES("content_type", "#{contentType}");
            VALUES("type", "#{type}");
            VALUES("password", "#{password}");
            VALUES("expire_timestamp", "#{expireTimestamp}");
        }}.toString();
    }

    public String queryByIdSqlBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_FILE_TABLE_NAME);
            WHERE("id = #{id}");
        }}.toString();
    }

    public String queryByUuidSqlBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_FILE_TABLE_NAME);
            WHERE("uuid = #{fileUuid}");
        }}.toString();
    }

    public String modifyByUuidSqlBuilder(Integer status, Long flag, byte[] password, Long expireTimestamp) {
        return new SQL() {{
            UPDATE(SANDCASTLE_FILE_TABLE_NAME);
            if (null != status) {
                SET("status = #{status}");
            }

            if (null != flag) {
                SET("flag = #{flag}");
            }

            if (null != password) {
                SET("password = #{password}");
            }

            if (null != expireTimestamp && expireTimestamp >= 0) {
                SET("expire_timestamp = #{expireTimestamp}");
            }

            WHERE("uuid = #{fileUuid}");
        }}.toString();
    }

}
