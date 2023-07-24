package com.fivesigma.backend.filter;

import com.fivesigma.backend.dto.UserDetailDTO;
import com.fivesigma.backend.service.impl.UserAuthServiceImpl;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.util.TokenUtil;
import com.fivesigma.backend.vo.UserAuthVO;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andy
 * @date 2022/10/8
 */

public class AuthenticationFilter extends BasicAuthenticationFilter {


    private ResponseUtil responseUtil = new ResponseUtil();
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserAuthServiceImpl userAuthService;
    @Autowired
    private JWTUtil jwtUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header_auth = request.getHeader("Authorization");
        //todo: no Authorization from header, never login
        if (header_auth == null || !header_auth.startsWith("Bearer ")){
            this.logger.trace("no token in header");
            chain.doFilter(request, response);
            responseUtil.filter_response(response, 403, "please login in", null);
            return;
        }
        String token = header_auth.replace("Bearer ", "");
        //todo: token in black list
        if (tokenUtil.inBlackList(token)){
            this.logger.trace("account logout");
            chain.doFilter(request, response);
            responseUtil.filter_response(response, 403, "account logout", null);
            return;
        }
        //todo: token validation (username and expire date)
        String username_token = jwtUtil.userInToken(token);
        UserDetailDTO userDetailDTO = (UserDetailDTO) userAuthService.loadUserByUsername(username_token);
        if (userDetailDTO == null || jwtUtil.isExpired(token)){
            this.logger.trace("token not legal");
            chain.doFilter(request, response);
            responseUtil.filter_response(response, 403, "token not legal", null);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username_token, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


}
