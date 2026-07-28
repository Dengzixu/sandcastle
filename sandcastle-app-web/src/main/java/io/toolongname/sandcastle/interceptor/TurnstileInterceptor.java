package io.toolongname.sandcastle.interceptor;

import io.toolongname.sandcastle.component.SecurityComponent;
import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.general.HumanMachineVerificationFailedException;
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
            throw new HumanMachineVerificationFailedException();
        }

        if (securityComponent.turnstileVerify(header)) {
            return true;
        } else {
            throw new HumanMachineVerificationFailedException();
        }
    }
}
