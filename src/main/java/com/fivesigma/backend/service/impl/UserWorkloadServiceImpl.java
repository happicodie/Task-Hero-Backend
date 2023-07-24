package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fivesigma.backend.dao.IUserWorkloadDao;
import com.fivesigma.backend.dto.UserWorkloadDTO;
import com.fivesigma.backend.po_entity.UserWorkload;
import com.fivesigma.backend.service.ITaskService;
import com.fivesigma.backend.service.IUserWorkloadService;
import com.fivesigma.backend.util.DateUtil;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.vo.TaskInfo_ResVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 * @date 2022/11/10
 */
@Service
public class UserWorkloadServiceImpl extends MPJBaseServiceImpl<IUserWorkloadDao, UserWorkload> implements IUserWorkloadService {
    @Autowired
    private IUserWorkloadDao userWorkloadDao;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean inOrUpWorkload(String token, Double user_available) {
        String user_id = jwtUtil.tryGetId(token);

        LambdaUpdateWrapper<UserWorkload> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserWorkload::getUser_available, user_available)
                    .eq(UserWorkload::getUser_id, user_id);

        if (userWorkloadDao.update(null, updateWrapper) > 0){
            return true;
        }
        UserWorkload userWorkload = new UserWorkload();
        userWorkload.setUser_id(user_id);
        userWorkload.setUser_available(user_available);
        if(userWorkloadDao.insert(userWorkload) > 0){
            return true;
        }
        return false;
    }

    /**
     * lazy update working hours aand busy rate when get workload info
     * @param target_id
     * @return
     */
    @Override
    public UserWorkloadDTO getWorkloadById(String target_id) {
        //todo: get target user tasks (to hin and to himself)
        List<TaskInfo_ResVO> task_related = new ArrayList<>();
        List<TaskInfo_ResVO> target_task1 = taskService.getTask(target_id, 1, null);
        List<TaskInfo_ResVO> target_task2 = taskService.getTask(target_id, 1, target_id);
        task_related.addAll(target_task1);
        task_related.addAll(target_task2);

        //todo: get target user old workload info
        MPJLambdaWrapper<UserWorkload> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(UserWorkload::getUser_id)
                .select(UserWorkload::getUser_available)
                .select(UserWorkload::getUser_working)
                .select(UserWorkload::getUser_busy)
                .eq(UserWorkload::getUser_id, target_id);

        UserWorkloadDTO  workloadDTO = userWorkloadDao.selectJoinOne(UserWorkloadDTO.class, wrapper);
        if (workloadDTO == null){
            return null;
        }
        //todo: get total working hours and estimate busy rate
        Double work_sum = 0.;
        for (TaskInfo_ResVO taskInfo : task_related){
            if (taskInfo.isAccept() &&
                ! taskInfo.getTask_status().equals("completed") &&
                DateUtil.inProgress(taskInfo.getEnd_date())){
                work_sum += taskInfo.getTask_workload();
            }
        }
        Double busy_rate = work_sum / workloadDTO.getUser_available();
        workloadDTO.setUser_working(work_sum);
        workloadDTO.setUser_busy(busy_rate);
        //todo: save new workload info
        LambdaUpdateWrapper<UserWorkload> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserWorkload::getUser_working, workloadDTO.getUser_working())
                .set(UserWorkload::getUser_busy, workloadDTO.getUser_busy())
                .eq(UserWorkload::getUser_id, workloadDTO.getUser_id());
        userWorkloadDao.update(null, updateWrapper);
        return workloadDTO;
    }

    @Override
    public UserWorkloadDTO getWorkloadByToken(String token) {
        String target_id = jwtUtil.tryGetId(token);
        return getWorkloadById(target_id);
    }
}
