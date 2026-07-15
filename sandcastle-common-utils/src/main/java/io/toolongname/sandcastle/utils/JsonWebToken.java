package io.toolongname.sandcastle.utils;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class JsonWebToken {
    private final int validityPeriod;
    private final SecretKey signKey;

    /**
     * 使用指定的参数创建一个新的 JSON Web Token 实例。
     *
     * @param validityPeriod 令牌保持有效的持续时间（以秒为单位）。
     * @param base64Secret   用于对令牌进行签名的 base64 编码密钥。
     * @param algorithm      用于签名的加密算法，例如 "HmacSHA512"。
     */
    public JsonWebToken(int validityPeriod, String base64Secret, String algorithm) {

        this.validityPeriod = validityPeriod;

        this.signKey = new SecretKeySpec(Base64.decodeBase64(base64Secret), algorithm);
    }

    public Encoder encoder() {
        return new JsonWebToken.Encoder();
    }

    public Decoder decoder(String jwtString) {
        return new JsonWebToken.Decoder(jwtString);
    }

    public class Encoder {
        private final JwtBuilder jwtBuilder;

        protected Encoder() {
            jwtBuilder = Jwts.builder()
                    .signWith(signKey)
                    .issuedAt(new Date())
                    .expiration(getExpireTime(validityPeriod));
        }

        public Encoder issuer(String issuer) {
            jwtBuilder.issuer(issuer);
            return this;
        }

        public Encoder subject(String sub) {
            jwtBuilder.subject(sub);
            return this;
        }

        public Encoder claim(String name, String value) {
            jwtBuilder.claim(name, value);
            return this;
        }

        public Encoder id(String id) {
            jwtBuilder.id(id);
            return this;
        }

        public String compact() {
            return jwtBuilder.compact();
        }
    }

    public class Decoder {
        private final String jwtString;
        private final JwtParser jwtParser;

        protected Decoder(String jwtString) {
            this.jwtString = jwtString;
            this.jwtParser = Jwts.parser().verifyWith(signKey).build();
        }

        public void verify() {
            try {
                jwtParser.parseSignedClaims(jwtString);
            } catch (JwtException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        public String getSubject() {
            return jwtParser.parseSignedClaims(jwtString).getPayload().getSubject();
        }

        public String getId() {
            return jwtParser.parseSignedClaims(jwtString).getPayload().getId();
        }

    }

    /**
     * 获取过期时间
     *
     * @param second 过期时间。单位：秒
     * @return 过期时间
     */
    private static Date getExpireTime(int second) {
        ZonedDateTime expireDateTim = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).plusSeconds(second);
        Date.from(expireDateTim.toInstant());

        return Date.from(expireDateTim.toInstant());

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.SECOND, second);
//
//        return calendar.getTime();
    }
}
