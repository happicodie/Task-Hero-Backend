package com.fivesigma.backend.controller;

import com.fivesigma.backend.service.impl.UserAuthServiceImpl;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.TokenVO;
import com.fivesigma.backend.vo.UserAuthVO;
import com.fivesigma.backend.vo.UserRegVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Andy
 * @date 2022/10/10
 */
@Api
@RestController
@RequestMapping("/userAuth")
public class UserAuthController {

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @ApiOperation("user register")
    @PostMapping("/register")
    public ResponseUtil<TokenVO> register(@RequestBody UserRegVO userRegVO){
        if (userAuthService.register(userRegVO)){
            //todo: generate token
            TokenVO tokenVO = userAuthService.login(userRegVO);
            return ResponseUtil.created("Create new user and login", tokenVO);
        }else {
            return ResponseUtil.ok("Existing user", null);
        }
    }
    @ApiOperation("user login")
    @PostMapping("/login")
    public ResponseUtil<TokenVO> login(@RequestBody UserAuthVO userAuthVO){
        TokenVO tokenVO = userAuthService.login(userAuthVO);
        if (tokenVO == null){
            return ResponseUtil.forbidden("false username or password", null);
        }
        return ResponseUtil.ok("login success", tokenVO);
    }

    @ApiOperation("test response entity")
    @PostMapping("/test_response")
    public ResponseUtil<String> test(){
        return ResponseUtil.created("is a entity", null  );
    }

    @ApiOperation("test hello")
    @PostMapping("/hello")
    public ResponseUtil<String> test_hello(){
        return ResponseUtil.created("hello springboot", null  );
    }

    @ApiOperation("forget password")
    @PostMapping("/forget")
    public ResponseUtil<String> forgetPwd(@RequestParam String user_email,
                                          @RequestParam String real_email){
        userAuthService.forgetPwd(user_email, real_email);
        return ResponseUtil.ok("send email success", null);
    }

}
