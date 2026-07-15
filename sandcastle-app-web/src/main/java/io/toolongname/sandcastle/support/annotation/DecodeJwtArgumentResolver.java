package io.toolongname.sandcastle.support.annotation;

import io.toolongname.sandcastle.property.JwtProperty;
import io.toolongname.sandcastle.utils.JsonWebToken;
import io.toolongname.sandcastlecommon.misc.annotation.JwtDecode;
import io.toolongname.sandcastlecommon.misc.exception.general.UnauthorizedException;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class DecodeJwtArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProperty jwtProperty;


    public DecodeJwtArgumentResolver(JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtDecode.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter,
                                            @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest,
                                            @Nullable WebDataBinderFactory binderFactory) throws Exception {


        String token = webRequest.getHeader("Authorization");

        boolean allowEmpty = parameter.getParameterAnnotation(JwtDecode.class).allowEmpty();


        if (null == token && allowEmpty) {
            throw new UnauthorizedException();
        }

        JsonWebToken jsonWebToken = new JsonWebToken(jwtProperty.validityPeriod(), jwtProperty.base64Secret(), jwtProperty.algorithm());

        try {
            return Optional.ofNullable(jsonWebToken.decoder(token).getSubject());
        } catch (RuntimeException e) {
            return Optional.empty();
        }

//        switch (parameter.getParameterType()) {
//            case Class<?> clazz when clazz == String.class -> {
//                System.out.println("String");
//            }
//            case Class<?> clazz when clazz == Optional.class -> {
//                System.out.println("Optional");
//            }
//            default -> throw new IllegalStateException("Unexpected value: " + parameter.getParameterType());
//        }
//
//        return null;
    }
}
