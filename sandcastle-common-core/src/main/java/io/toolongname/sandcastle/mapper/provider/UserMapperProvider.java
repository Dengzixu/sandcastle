package io.toolongname.sandcastle.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class UserMapperProvider {
    private static final String SANDCASTLE_USER_TABLE_NAME = "sandcastle_user";


    public String addSQLBuilder() {
        return new SQL() {{
            INSERT_INTO(SANDCASTLE_USER_TABLE_NAME);
            VALUES("uuid", "#{uuid}");
            VALUES("status", "#{status}");
            VALUES("username", "#{username}");
            VALUES("email", "#{email}");
            VALUES("password", "#{password}");
        }}.toString();
    }

    public String queryByUUIDSQLBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_USER_TABLE_NAME);
            WHERE("uuid = #{uuid}");
        }}.toString();
    }

    public String queryByEmailSQLBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_USER_TABLE_NAME);
            WHERE("email = #{email}");
        }}.toString();
    }

    public String queryByUsernameSQLBuilder() {
        return new SQL() {{
            SELECT("*");
            FROM(SANDCASTLE_USER_TABLE_NAME);
            WHERE("username = #{username}");
        }}.toString();
    }

}
