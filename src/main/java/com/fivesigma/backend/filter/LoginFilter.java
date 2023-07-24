package com.fivesigma.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivesigma.backend.dto.UserDetailDTO;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.UserAuthVO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andy
 * @date 2022/10/8
 */

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//    @Autowired
//    private JWTUtil jwtUtil;
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private ResponseUtil responseUtil;
//
//    public LoginFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            UserAuthVO userAuthVO = new ObjectMapper().readValue(request.getInputStream(), UserAuthVO.class);
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            userAuthVO.getUsername(),
//                            userAuthVO.getPassword(),
//                            new ArrayList<>()
//                    )
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        String token = jwtUtil.generateToken(authResult);
//        response.addHeader("Authorization", "Bearer " + token);
//        responseUtil.filter_response(response, 200, "login success", null);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        responseUtil.filter_response(response, 403, "login fail", null);
//    }
}
