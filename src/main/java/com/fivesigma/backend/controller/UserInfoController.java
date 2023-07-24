package com.fivesigma.backend.controller;

import com.fivesigma.backend.service.IUserInfoService;
import com.fivesigma.backend.service.IUserTagService;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.AvailTagsVO;
import com.fivesigma.backend.vo.EditProfileVO;
import com.fivesigma.backend.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/11
 */

@Api
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;
//    @Autowired
//    private IConnectionService connectionService;
    @Autowired
    private IUserTagService userTagService;


    @GetMapping("/testAuth")
    public ResponseUtil<String> test(){
        return ResponseUtil.ok("auth success", "auth success!");
    }

    @ApiOperation("get user profile by token")
    @GetMapping("/profile")
    public ResponseUtil<UserInfoVO> getInfoByToken(@ApiIgnore @RequestHeader("Authorization") String token){
        UserInfoVO userInfoVO = userInfoService.getInfoByToken(token);
        if (userInfoVO == null){
            return ResponseUtil.not_found("get info fail", null);
        }
        return ResponseUtil.ok("get info success", userInfoVO);
    }

//    @ApiOperation("get user profile by email")
//    @GetMapping("/profile/email/{email}")
//    public ResponseUtil<UserInfoVO> getInfoByEmail(@PathVariable(name = "email") String email){
//        System.out.println(email);
//        UserInfoVO userInfoVO = userInfoService.getInfoByUsername(email);
//        if (userInfoVO == null){
//            return ResponseUtil.not_found("get info fail", null);
//        }
//        return ResponseUtil.ok("get info success", userInfoVO);
//    }

    @ApiOperation("get user profile by id")
    @GetMapping("/profile/{id}")
    public ResponseUtil<UserInfoVO> getInfoById(@ApiIgnore @RequestHeader("Authorization") String token,
                                                @PathVariable(name = "id") String id){
//        System.out.println(id);
        UserInfoVO userInfoVO = userInfoService.getInfoByUserId(token, id, true);
        if (userInfoVO == null){
            return ResponseUtil.not_found("user: " + id + " not exist", null);
        }
        return ResponseUtil.ok("get info success", userInfoVO);
    }

    @ApiOperation("edit user profile")
    @PutMapping("/profile")
    public ResponseUtil<UserInfoVO> editInfoByToken(@ApiIgnore @RequestHeader("Authorization") String token,
                                                    @RequestBody EditProfileVO editProfileVO){
        try {
            userInfoService.editInfoByToken(token, editProfileVO);
            return ResponseUtil.ok("edit success", null);
        } catch (Exception e) {
            return ResponseUtil.forbidden("edit fail", null);
        }
    }

//    @ApiOperation("get user connected")
//    @GetMapping("/connected")
//    public ResponseUtil<List<ConnectionVO>> getConnectedByToken(@RequestHeader("Authorization") String token){
//        List<ConnectionVO> connectionVOList = connectionService.getConnectedByToken(token);
//        return ResponseUtil.ok("get connection success", connectionVOList)  ;
//    }

    @ApiOperation("edit user profile")
    @GetMapping("/availtags")
    public ResponseUtil<List<AvailTagsVO>> getAvailTags(){
        List<AvailTagsVO> availTagsVOList = null;
        try {
            availTagsVOList = userTagService.getAvailTag();
            return ResponseUtil.ok("get tags success", availTagsVOList);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", availTagsVOList);
        }
    }

}
