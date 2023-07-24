package com.fivesigma.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * @author Andy
 * @date 2022/10/9
 */

@Component
public class TokenUtil {

    @Autowired
    private JWTUtil jwtUtil;
    private static final HashSet<String> blackList = new HashSet<>();

    public void addBlackList(String token){
        blackList.add(token);
    }
    public boolean inBlackList(String token){
        return blackList.contains(token);
    }

    //todo: clear expire token regularly
    public void flushBlackList(){

    }
}
