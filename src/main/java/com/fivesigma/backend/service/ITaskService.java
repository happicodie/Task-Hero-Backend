package com.fivesigma.backend.service;

import com.fivesigma.backend.dto.TaskRelDTO;
import com.fivesigma.backend.po_entity.Log;
import com.fivesigma.backend.po_entity.TaskInfo;
import com.fivesigma.backend.vo.*;

import java.util.List;

public interface ITaskService {

    TaskCreateVO createTask(String token, TaskInfo_ReqVO taskInfo_reqVO);
    List<Log> editTask(String token, String task_id, TaskInfo_ReqVO taskInfo_reqVO);
    List<TaskRelDTO> getTaskId(String assigner_id, String assignee_id);
    List<TaskInfo_ResVO> getTask(String token, Integer toWho, String other_id);
    List<TaskInfo_ResVO> getTaskToOther(String token);
    List<TaskInfo_ResVO> getTaskToOtherComplete(String token);
    List<TaskInfo_ResVO> getTaskToMe(String token);
    List<TaskInfo_ResVO> getTaskToMyself(String token);
    int acceptTask(String token, String task_id);
    int declineTask(String token, String task_id);
    boolean setStatus(String token, EditStatusVO editStatusVO);
    boolean setFeedback(String token, SetFeedbackVO setFeedbackVO);
    List<TaskInfo_ResVO> searchTaskOr(String token, String[] fields, Object target, int[] toWho);
    List<TaskInfo_ResVO> searchTaskAnd(String token, TaskSearchVO searchVO, int[] toWho);
    TaskInfo_ResVO getTaskById(String token, String task_id);
    boolean autoUpdateStatus(TaskInfo taskInfo, TaskRelDTO taskRelDTO);
    List<Log> writeLog(String task_id, String log);
}
