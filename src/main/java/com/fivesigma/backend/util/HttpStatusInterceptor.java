package com.fivesigma.backend.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Andy
 * @date 2022/10/19
 */

@ControllerAdvice
public class HttpStatusInterceptor implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
//        return true;
        return !returnType.getDeclaringClass().getName().contains("springfox");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        int code = ((ResponseUtil)body).getCode();
        response.setStatusCode(HttpStatus.valueOf(code));
        return body;
    }
}
