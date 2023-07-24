package com.fivesigma.backend.filter;

import com.fivesigma.backend.dto.UserDetailDTO;
import com.fivesigma.backend.service.impl.UserAuthServiceImpl;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andy
 * @date 2022/10/11
 */

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserAuthServiceImpl userAuthService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header_auth = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (header_auth != null && header_auth.startsWith("Bearer")){
            token = header_auth.replace("Bearer ", "");
            username = jwtUtil.userInToken(token);
        }
//        else {
//            response.sendError(403, "please login in!");
//            filterChain.doFilter(request, response);
////            responseUtil.filter_response(response, 403, "please login in", null);
//            return;
//        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userAuthService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
