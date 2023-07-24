package com.fivesigma.backend.service.impl;

import com.fivesigma.backend.service.IConnectionService;
import com.fivesigma.backend.service.ISearchService;
import com.fivesigma.backend.service.ITaskService;
import com.fivesigma.backend.service.IUserInfoService;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @author Andy
 * @date 2022/11/3
 */

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IConnectionService connectionService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void checkTaskAuth(String token, List<TaskInfo_ResVO> taskInfo_resVOS) {
        token = token.replace("Bearer ", "");
        String token_id = jwtUtil.userIdInToken(token);
        Iterator<TaskInfo_ResVO> iterator = taskInfo_resVOS.listIterator();
        while (iterator.hasNext()) {
            TaskInfo_ResVO info = iterator.next();
            String assigner_id = info.getAssigner_id();
            String assignee_id = info.getAssignee_id();
            if (assigner_id != token_id && connectionService.checkRelation(token, assigner_id).equals("connected")){
                iterator.remove();
                continue;
            }
            if (assignee_id != token_id && connectionService.checkRelation(token, assignee_id).equals("connected")){
                iterator.remove();
                continue;
            }
        }
    }

    @Override
    public SearchGlobalVO globalSearch(String token, String search_key) {
        SearchGlobalVO searchGlobalVO = new SearchGlobalVO();
        List<UserInfoVO> infoVOS = userInfoService.getInfoFull(new String[]{"user_id", "user_name", "user_email"}, search_key);
        for (UserInfoVO userInfoVO : infoVOS){
            userInfoVO.setRelation(connectionService.checkRelation(token, userInfoVO.getUser_id()));
        }

        List<TaskInfo_ResVO> taskInfosToOther = taskService.searchTaskOr(token, new String[]{"task_id", "task_name", "task_desc"}, search_key, new int[]{0});
        List<TaskInfo_ResVO> taskInfosToMe = taskService.searchTaskOr(token, new String[]{"task_id", "task_name", "task_desc"}, search_key, new int[]{1});
        List<TaskInfo_ResVO> taskInfosToMyself = taskService.searchTaskOr(token, new String[]{"task_id", "task_name", "task_desc"}, search_key, new int[]{2});

//        //todo: check if have right to see tasks (only partners tasks can be searched)
//        checkTaskAuth(token, taskInfosToOther);
//        checkTaskAuth(token, taskInfosToMe);

        searchGlobalVO.setUserInfoVOS(infoVOS);
        searchGlobalVO.setTaskInfosToOther(taskInfosToOther);
        searchGlobalVO.setTaskInfosToMe(taskInfosToMe);
        searchGlobalVO.setTaskInfosToMyself(taskInfosToMyself);
        return searchGlobalVO;
    }

    @Override
    public List<UserInfoVO> searchUser(UserSearchVO userSearchVO) {
        return userInfoService.getInfoFullPrecise(userSearchVO);
    }

    @Override
    public List<TaskInfo_ResVO> searchTask(String token, TaskSearchVO taskSearchVO) {
        return taskService.searchTaskAnd(token, taskSearchVO, new int[]{0,1,2});
    }
}
