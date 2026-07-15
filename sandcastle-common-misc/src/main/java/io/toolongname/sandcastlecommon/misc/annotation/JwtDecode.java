package io.toolongname.sandcastlecommon.misc.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtDecode {
    boolean allowEmpty() default false;
}
