package com.fivesigma.backend.controller;

import com.fivesigma.backend.dto.UserWorkloadDTO;
import com.fivesigma.backend.service.IUserWorkloadService;
import com.fivesigma.backend.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Andy
 * @date 2022/11/11
 */
@Api
@RestController("/workload")
public class UserWorkloadController {
    @Autowired
    private IUserWorkloadService userWorkloadService;

    @ApiOperation("edit available hours")
    @PostMapping("/available")
    public ResponseUtil<String> setAvailable(@ApiIgnore @RequestHeader("Authorization") String token,
                                             @RequestParam Double ava_hour){
        if (userWorkloadService.inOrUpWorkload(token, ava_hour)){
            return ResponseUtil.ok("edit available hour success", null);
        }else {
            return ResponseUtil.forbidden("edit available hour fail", null);
        }
    }

    @ApiOperation("get busy estimate")
    @PostMapping("/busyEstimate")
    public ResponseUtil<UserWorkloadDTO> getWorkload(@RequestParam String user_id){
        UserWorkloadDTO res = userWorkloadService.getWorkloadById(user_id);
        if (res == null){
            return ResponseUtil.forbidden("get workload fail", null);
        }else {
            return ResponseUtil.ok("get workload success", res);
        }
    }
}
