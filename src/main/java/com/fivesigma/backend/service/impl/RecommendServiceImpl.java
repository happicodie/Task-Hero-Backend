package com.fivesigma.backend.service.impl;

import com.fivesigma.backend.service.IRecommendService;
import com.fivesigma.backend.service.ITaskService;
import com.fivesigma.backend.service.IUserInfoService;
import com.fivesigma.backend.util.TaskComparator;
import com.fivesigma.backend.vo.TaskInfo_ResVO;
import com.fivesigma.backend.vo.TaskSearchVO;
import com.fivesigma.backend.vo.UserInfoVO;
import com.fivesigma.backend.vo.UserRecommendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andy
 * @date 2022/11/11
 */
@Service
public class RecommendServiceImpl implements IRecommendService {

    @Autowired
    private ITaskService taskService;
    @Autowired
    private TaskComparator taskComparator;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public List<UserRecommendVO> topOnTask(String token, String tag, Integer top_count){
        //todo: get all completed tasks base on tag
        TaskSearchVO taskSearchVO = new TaskSearchVO();
        if (tag != null){
            taskSearchVO.setTask_tag(tag);
        }
        taskSearchVO.setTask_status("completed");
        List<TaskInfo_ResVO> tasks = taskService.searchTaskAnd(null, taskSearchVO, new int[]{0});
//        tasks.sort(taskComparator);

        Map<String, List<Integer>> user_scores = new HashMap<>();
        for (TaskInfo_ResVO task : tasks){
            String user_id = task.getAssignee_id();
            List<Integer> scores = user_scores.getOrDefault(user_id, new ArrayList<Integer>());
            scores.add(task.getTask_score());
            user_scores.put(user_id, scores);
        }
        Map<String, Double> user_mean = new HashMap<>();
        for (Map.Entry<String,List<Integer>> entry : user_scores.entrySet()){
            String user_id = entry.getKey();
            List<Integer> scores = entry.getValue();
            Integer score_sum = scores.stream().mapToInt(Integer::intValue).sum();
            user_mean.put(user_id, score_sum * 1.0 / scores.size());
        }
        Map<String, Double> mean_sorted = user_mean.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        List<UserRecommendVO> res = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Double> entry: mean_sorted.entrySet()){
            if (count == top_count) break;
            String user_id = entry.getKey();
            Double avg_score = entry.getValue();
            UserInfoVO userInfoVO = userInfoService.getInfoByUserId(token, user_id, false);
            res.add(new UserRecommendVO(userInfoVO, avg_score));
            count ++;
        }
        return res;
    }

    @Override
    public List<UserRecommendVO> topOnUser() {
        return null;
    }
}
