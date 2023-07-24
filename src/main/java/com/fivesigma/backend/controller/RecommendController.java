package com.fivesigma.backend.controller;

import com.fivesigma.backend.service.IRecommendService;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.UserRecommendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Andy
 * @date 2022/11/11
 */
@Api
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private IRecommendService recommendService;

    @ApiOperation("recommend by task tag")
    @GetMapping("/{task_tag}")
    public ResponseUtil<List<UserRecommendVO>> recByTaskTag(@ApiIgnore @RequestHeader("Authorization") String token,
                                                            @PathVariable(name = "task_tag") String task_tag){
        List<UserRecommendVO> res = recommendService.topOnTask(token, task_tag, 10);
        if (res == null){
            return ResponseUtil.forbidden("get recommend fail", null);
        }else {
            return ResponseUtil.ok("get recommend success", res);
        }
    }

    @ApiOperation("recommend by user")
    @GetMapping("/")
    public ResponseUtil<List<UserRecommendVO>> recByUser(@ApiIgnore @RequestHeader("Authorization") String token){
        List<UserRecommendVO> res = recommendService.topOnTask(token, null, 10);
        if (res == null){
            return ResponseUtil.forbidden("get recommend fail", null);
        }else {
            return ResponseUtil.ok("get recommend success", res);
        }
    }
}
