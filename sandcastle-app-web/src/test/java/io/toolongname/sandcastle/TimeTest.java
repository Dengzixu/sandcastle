package io.toolongname.sandcastle;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeTest {

    @Test
    void testTime() {
        // 计算过期时间
        LocalDateTime expire = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).plusDays(7);

        Date d = Date.from(expire.toInstant(ZoneOffset.of("+08:00")));

        System.out.println(d);


        System.out.println(ZonedDateTime.now().toInstant().toEpochMilli());
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+08:00")));

    }
}
