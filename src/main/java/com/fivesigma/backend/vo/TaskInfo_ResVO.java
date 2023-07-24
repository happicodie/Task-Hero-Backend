package com.fivesigma.backend.vo;

import com.fivesigma.backend.dto.TaskRelDTO;
import com.fivesigma.backend.po_entity.CheckList;
import com.fivesigma.backend.po_entity.Log;
import com.fivesigma.backend.po_entity.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/29
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo_ResVO {
    private String task_id;
    private String task_name;
    private String assigner_id;
    private String assignee_id;
    private String start_date;
    private String end_date;
    private String task_status;
    private String task_tag;
    private String task_desc;
    private String task_priority;
    private Double task_workload;
    private List<CheckList> checkListList;
    private List<Log> logs;
    private boolean accept;
    private Integer task_score;
    private String task_feedback;

    public TaskInfo_ResVO(TaskInfo taskInfo, TaskRelDTO taskRelDTO){
        this.task_id = taskRelDTO.getTask_id();
        this.task_name = taskInfo.getTask_name();
        this.assigner_id = taskRelDTO.getAssigner_id();
        this.assignee_id = taskRelDTO.getAssignee_id();
        this.start_date = taskInfo.getStart_date();
        this.end_date = taskInfo.getEnd_date();
        this.task_status = taskInfo.getTask_status();
        this.task_tag = taskInfo.getTask_tag();
        this.task_desc = taskInfo.getTask_desc();
        this.task_priority = taskInfo.getTask_priority();
        this.task_workload = taskInfo.getTask_workload();
        this.checkListList = taskInfo.getCheckListList();
        this.logs = taskInfo.getLogs();
        this.accept = taskRelDTO.getAccept();

        this.task_score = taskInfo.getTask_score();
        this.task_feedback = taskInfo.getTask_feedback();
    }
}
