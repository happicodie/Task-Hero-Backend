package com.fivesigma.backend.service;

import com.fivesigma.backend.vo.*;

import java.util.List;

public interface ISearchService {
    SearchGlobalVO globalSearch(String token, String search_key);
    void checkTaskAuth(String token, List<TaskInfo_ResVO> taskInfo_resVOS);
    List<UserInfoVO> searchUser(UserSearchVO userSearchVO);
    List<TaskInfo_ResVO> searchTask(String token, TaskSearchVO taskSearchVO);
}
