package com.fivesigma.backend.controller;

import com.fivesigma.backend.service.ISearchService;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/17
 */

@Api
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ISearchService searchService;

    @ApiOperation("global search by one key-word")
    @GetMapping("/global")
    public ResponseUtil<SearchGlobalVO> globalSearch(@ApiIgnore @RequestHeader("Authorization") String token,
                                                     @RequestParam String key){
        SearchGlobalVO res = searchService.globalSearch(token, key);
        if (res == null){
            return ResponseUtil.forbidden("global search fail", null);
        }else {
            return ResponseUtil.ok("global search success", res);
        }
    }

    @ApiOperation("precise search on user")
    @PostMapping("/user")
    public ResponseUtil<List<UserInfoVO>> searchUser(@RequestBody UserSearchVO userSearchVO){
        List<UserInfoVO> res = searchService.searchUser(userSearchVO);
        if (res == null){
            return ResponseUtil.forbidden("search user fail", null);
        }else {
            return ResponseUtil.ok("search user success", res);
        }
    }

    @ApiOperation("precise search on task")
    @PostMapping("/task")
    public ResponseUtil<List<TaskInfo_ResVO>> searchTask(@ApiIgnore @RequestHeader("Authorization") String token,
                                                         @RequestBody TaskSearchVO taskSearchVO){
        List<TaskInfo_ResVO> res = searchService.searchTask(token, taskSearchVO);
        if (res == null){
            return ResponseUtil.forbidden("search task fail", null);
        }else {
            return ResponseUtil.ok("search task success", res);
        }
    }
}
