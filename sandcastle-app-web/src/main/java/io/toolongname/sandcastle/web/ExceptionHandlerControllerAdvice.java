package io.toolongname.sandcastle.web;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(exception = ServletException.class)
    public ProblemDetail ServletExceptionHandler(ServletException servletException, HttpServletRequest req, HttpServletResponse resp) {
        return switch (servletException) {
            case NoResourceFoundException _ -> ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "路由不存在");
            case MissingRequestHeaderException e ->
                    ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "缺少请求头 [" + e.getHeaderName() + "]");
            default ->
                    ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, servletException.getMessage());
        };
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errorFields = new HashMap<>();
        e.getFieldErrors().forEach(fieldError -> {
            errorFields.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ErrorCode.DATA_VALIDATION_FAILED.message());
        problemDetail.setProperty("error_code", ErrorCode.DATA_VALIDATION_FAILED.code());
        problemDetail.setProperty("error_fields", errorFields);

        return problemDetail;
    }

    @ExceptionHandler(exception = BusinessException.class)
    public ProblemDetail businessExceptionHandler(BusinessException e) {
        ErrorCode code = e.getErrorCode();

        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        problemDetail.setProperty("error_code", code.code());

        return problemDetail;
    }
}
