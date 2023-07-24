package com.fivesigma.backend.controller;

import com.fivesigma.backend.po_entity.Log;
import com.fivesigma.backend.service.ITaskService;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 * @date 2022/10/29
 */
@Api
@RestController
@RequestMapping("/task")
public class  TaskController {
    @Autowired
    private ITaskService taskService;

    @ApiOperation("create new task by token")
    @PostMapping("/create")
    public ResponseUtil<TaskCreateVO> createTask(@ApiIgnore @RequestHeader("Authorization") String token,
                                                 @RequestBody TaskInfo_ReqVO taskInfo_reqVO){
        TaskCreateVO taskCreateVO = taskService.createTask(token, taskInfo_reqVO);
        if (taskCreateVO == null){
            return ResponseUtil.forbidden("create task fail", null);
        }else {
            return ResponseUtil.created("create task success", taskCreateVO);
        }
    }

    @ApiOperation("task task by task id")
    @PutMapping("/edit/{task_id}")
    public ResponseUtil<List<Log>> editTask(@ApiIgnore @RequestHeader("Authorization") String token,
                                            @PathVariable(name = "task_id") String task_id,
                                            @RequestBody TaskInfo_ReqVO taskInfo_reqVO){
        try {
            List<Log> logs = taskService.editTask(token, task_id, taskInfo_reqVO);
            return ResponseUtil.ok("edit task success", logs);
        } catch (Exception e) {
            return ResponseUtil.forbidden("edit task fail", null);
        }
    }

    @ApiOperation("get task assign to other")
    @GetMapping("/other")
    public ResponseUtil<List<TaskInfo_ResVO>> getTaskToOther(@ApiIgnore @RequestHeader("Authorization") String token){
//        List<TaskInfo_ResVO> taskInfo_resVOS = taskService.getTask(token, 0, null);
        List<TaskInfo_ResVO> taskInfo_resVOS = taskService.getTaskToOther(token);
        if (taskInfo_resVOS == null){
            return ResponseUtil.forbidden("get tasks fail", null);
        }else {
            return ResponseUtil.ok("get tasks success", taskInfo_resVOS);
        }
    }

    @ApiOperation("get task assign to other which is completed")
    @GetMapping("/other/complete")
    public ResponseUtil<List<TaskInfo_ResVO>> getTaskToOtherComplete(@ApiIgnore @RequestHeader("Authorization") String token){
//        List<TaskInfo_ResVO> taskInfo_resVOS = taskService.searchTaskOr(token, new String[]{"task_status"}, "Completed", new int[]{0});
        List<TaskInfo_ResVO> taskInfo_resVOS = taskService.getTaskToOtherComplete(token);
        if (taskInfo_resVOS == null){
            return ResponseUtil.forbidden("get tasks fail", null);
        }else {
            return ResponseUtil.ok("get tasks success", taskInfo_resVOS);
        }
    }


    @ApiOperation("get task assign to me")
    @GetMapping("/me")
    public ResponseUtil<List<TaskInfo_ResVO>> getTaskToMe(@ApiIgnore @RequestHeader("Authorization") String token){
//        List<TaskInfo_ResVO> taskAssignToMe = taskService.getTask(token, 1, null);
        List<TaskInfo_ResVO> taskAssignToMe = taskService.getTaskToMe(token);
        List<TaskInfo_ResVO> taskAssignToMyself = taskService.getTaskToMyself(token);
        List<TaskInfo_ResVO> res = new ArrayList<>();
        res.addAll(taskAssignToMe);
        res.addAll(taskAssignToMyself);
        if (res == null){
            return ResponseUtil.forbidden("get tasks fail", null);
        }else {
            return ResponseUtil.ok("get tasks success", res);
        }
    }

    @ApiOperation("get task assign to myself")
    @GetMapping("/myself")
    public ResponseUtil<List<TaskInfo_ResVO>> getTaskToMyself(@ApiIgnore @RequestHeader("Authorization") String token){

        List<TaskInfo_ResVO> taskAssignToMyself = taskService.getTaskToMyself(token);

        if (taskAssignToMyself == null){
            return ResponseUtil.forbidden("get tasks fail", null);
        }else {
            return ResponseUtil.ok("get tasks success", taskAssignToMyself);
        }
    }

    @ApiOperation("accept task by task id")
    @GetMapping("/accept/{task_id}")
    public ResponseUtil<String> acceptTask(@ApiIgnore @RequestHeader("Authorization") String token,
                                           @PathVariable(name = "task_id") String task_id){
//        try {
//            taskService.acceptTask(task_id);
//            return ResponseUtil.ok("accept success", null);
//        } catch (Exception e) {
//            return ResponseUtil.forbidden("accept fail", null);
//        }
        if (taskService.acceptTask(token, task_id) == 0){
            return ResponseUtil.forbidden("accept fail", null);
        }else {
            return ResponseUtil.ok("accept success", null);
        }
    }

    @ApiOperation("decline task by task id")
    @GetMapping("/decline/{task_id}")
    public ResponseUtil<String> declineTask(@ApiIgnore @RequestHeader("Authorization") String token,
                                            @PathVariable(name = "task_id") String task_id){
//        try {
//            taskService.declineTask(task_id);
//            return ResponseUtil.ok("accept success", null);
//        } catch (Exception e) {
//            return ResponseUtil.forbidden("accept fail", null);
//        }
        if (taskService.declineTask(token, task_id) == 0){
            return ResponseUtil.forbidden("decline fail", null);
        }else {
            return ResponseUtil.ok("decline success", null);
        }
    }

    @ApiOperation("get task by token and status")
    @GetMapping("status/{status}")
    public ResponseUtil<List<TaskInfo_ResVO>> getTaskByStatus(@ApiIgnore @RequestHeader("Authorization") String token,
                                                              @PathVariable(name = "status") String status){
        List<TaskInfo_ResVO> res = taskService.searchTaskOr(token, new String[]{"task_status"}, status, new int[]{1,2});
        if (res == null){
            return ResponseUtil.forbidden("get task fail", null);
        }else {
            return ResponseUtil.ok("get task success", res);
        }
    }

    @ApiOperation("get task by token and task_id")
    @GetMapping("taskId/{task_id}")
    public ResponseUtil<TaskInfo_ResVO> getTaskById(@ApiIgnore @RequestHeader("Authorization") String token,
                                                    @PathVariable(name = "task_id") String task_id){
        TaskInfo_ResVO res = taskService.getTaskById(token, task_id);
        if (res == null){
            return ResponseUtil.forbidden("get task fail", null);
        }else {
            return ResponseUtil.ok("get task success", res);
        }
    }


    @ApiOperation("set status by task id")
    @PostMapping("/status")
    public ResponseUtil<String> editStatus(@ApiIgnore @RequestHeader("Authorization") String token,
                                           @RequestBody EditStatusVO editStatusVO){
        if (taskService.setStatus(token, editStatusVO)){
            return ResponseUtil.ok("set status success", null);
        }else {
            return ResponseUtil.forbidden("set status fail", null);
        }
    }

    @ApiOperation("set feedback by task id")
    @PostMapping("/feedback")
    public ResponseUtil<String> setFeedback(@ApiIgnore @RequestHeader("Authorization") String token,
                                            @RequestBody SetFeedbackVO setFeedbackVO){
        if (taskService.setFeedback(token, setFeedbackVO)){
            return ResponseUtil.ok("set feedback success", null);
        }else {
            return ResponseUtil.forbidden("set feedback fail", null);
        }
    }
}
