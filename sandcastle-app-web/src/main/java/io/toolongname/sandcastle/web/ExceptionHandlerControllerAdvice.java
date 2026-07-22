package io.toolongname.sandcastle.web;

import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(exception = ServletException.class)
    public ResponseEntity<ResponseData> a(ServletException servletException, HttpServletRequest req, HttpServletResponse resp) {
        return switch (servletException) {
            case NoResourceFoundException _ ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseData.FAILED("路由 [" + req.getServletPath() + "] 不存在"));
            case MissingRequestHeaderException e ->
                    ResponseEntity.badRequest().body(ResponseData.FAILED("缺少请求头 [" + e.getHeaderName() + "]"));
            default ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.FAILED("未知错误, " + servletException.getMessage()));
        };
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errorMsg = new HashMap<>();
        e.getFieldErrors().forEach(fieldError -> {
            errorMsg.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity
                .badRequest()
                .body(new ResponseData(ResponseData.Status.FAILED,
                        ErrorCode.DATA_VALIDATION_FAILED.code(),
                        ErrorCode.DATA_VALIDATION_FAILED.message(),
                        errorMsg));
    }

    @ExceptionHandler(exception = BusinessException.class)
    public ResponseEntity<ResponseData> businessExceptionHandler(BusinessException e) {
        ErrorCode code = e.getErrorCode();

        return ResponseEntity.status(e.getHttpStatus())
                .body(ResponseData.FAILED(code.message(), code.code()));
    }
}
