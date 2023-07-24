package com.fivesigma.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Andy
 * @date 2022/10/7
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUtil<T> {

//    private Boolean flag;
    private Integer code;
    private String message;
    private T data;
//    private HttpStatus httpStatus;

    public static <T> ResponseUtil<T> ok(String message, T data) {
        return new ResponseUtil<>(200, message, data);
    }

    public static <T> ResponseUtil<T> created(String message, T data) {
        return new ResponseUtil<>(201, message, data);
    }

    public static <T> ResponseUtil<T> unauthorized(String message, T data) {
        return new ResponseUtil<>(401, message, data);
    }

    public static <T> ResponseUtil<T> forbidden(String message, T data) {
        return new ResponseUtil<>(403, message, data);
    }

    public static <T> ResponseUtil<T> not_found(String message, T data) {
        return new ResponseUtil<>(404, message, data);
    }

    public void filter_response(HttpServletResponse response, Integer code, String message, T Data){
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(code);
//            response.
            PrintWriter out = response.getWriter();
            HashMap<String,Object> map = new HashMap<>();
            map.put("code",code);
            map.put("message", message);
            map.put("data", data);
            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
