package io.toolongname.sandcastle.interceptor;

import io.toolongname.sandcastle.component.SecurityComponent;
import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

@Component
public class TurnstileInterceptor implements HandlerInterceptor {
    private final SecurityComponent securityComponent;

    public TurnstileInterceptor(SecurityComponent securityComponent) {
        this.securityComponent = securityComponent;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("x-cloudflare-turnstile");

        if (null == header || header.isBlank()) {
            ResponseData responseData = ResponseData
                    .FAILED(ErrorCode.HUMAN_MACHINE_VERIFICATION_FAILED.message(), ErrorCode.HUMAN_MACHINE_VERIFICATION_FAILED.code());

            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }

        if (securityComponent.turnstileVerify(header)) {
            return true;
        } else {
            ResponseData responseData = ResponseData
                    .FAILED(ErrorCode.HUMAN_MACHINE_VERIFICATION_FAILED.message(), ErrorCode.HUMAN_MACHINE_VERIFICATION_FAILED.code());

            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }
    }
}
