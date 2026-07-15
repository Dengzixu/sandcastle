package io.toolongname.sandcastle.filter;

import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastle.property.JwtProperty;
import io.toolongname.sandcastle.utils.JsonWebToken;
import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@WebFilter(filterName = "TokenFilter", urlPatterns = {"/user/api/v1/token/verify"})
public class TokenFilter extends OncePerRequestFilter {
    private static final List<String> whiteList = new ArrayList<>() {{
        add("/user/api/v1/register");
        add("/user/api/v1/login");
    }};

    private final JwtProperty jwtProperty;

    public TokenFilter(JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

//        if (whiteList.contains(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            JsonWebToken jsonWebToken = new JsonWebToken(jwtProperty.issuer(),
//                    jwtProperty.validityPeriod(),
//                    jwtProperty.base64Secret(),
//                    jwtProperty.algorithm());
//
//            jsonWebToken.decoder(token).verify();
//        } catch (RuntimeException e) {
//            ResponseData responseData = ResponseData.FAILED(ErrorCode.TOKEN_INVALID.message(),
//                    ErrorCode.TOKEN_INVALID.code());
//
//            response.setHeader("Content-Type", "application/json;charset=utf-8");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//
//            return;
//        }

        filterChain.doFilter(request, response);
    }

}
