package com.fivesigma.backend.po_entity;

import com.fivesigma.backend.vo.TaskInfo_ReqVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/28
 */

/**
 * update: replace mysql with mongodb
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("task_table")
@Document(collection = "taskInfo")
public class TaskInfo {
    @Id
    private String task_id;
    private String task_status;
    private String start_date;
    private String end_date;
    private String task_tag;
    private String task_name;
    private String task_desc;
    private String task_priority;
    private Double task_workload;

    private Integer task_score;
    private String task_feedback;

    private List<CheckList> checkListList;
    private List<Log> logs;

    public TaskInfo(TaskInfo_ReqVO taskInfo_reqVO){
        this.task_name = taskInfo_reqVO.getTask_name();
        this.start_date = taskInfo_reqVO.getStart_date();
        this.end_date = taskInfo_reqVO.getEnd_date();
        this.task_status = taskInfo_reqVO.getTask_status();
        this.task_tag = taskInfo_reqVO.getTask_tag();
        this.task_desc = taskInfo_reqVO.getTask_desc();
        this.task_priority = taskInfo_reqVO.getTask_priority();
        this.task_workload = taskInfo_reqVO.getTask_workload();
        this.checkListList = taskInfo_reqVO.getCheckListList();
    }

    public void updateInfo(TaskInfo_ReqVO taskInfo_reqVO){
//        List<String> changes = new ArrayList<>();
//        String task_name = taskInfo_reqVO.getTask_name();
//        String start_date = taskInfo_reqVO.getStart_date();
//        String end_date = taskInfo_reqVO.getEnd_date();
//        String task_status = taskInfo_reqVO.getTask_status();
//        String task_tag = taskInfo_reqVO.getTask_tag();
//        String task_desc = taskInfo_reqVO.getTask_desc();
//        String task_priority = taskInfo_reqVO.getTask_priority();
//        Double task_workload = taskInfo_reqVO.getTask_workload();
//        String checkListList = taskInfo_reqVO.getCheckListList();

        this.task_name = taskInfo_reqVO.getTask_name();
        this.start_date = taskInfo_reqVO.getStart_date();
        this.end_date = taskInfo_reqVO.getEnd_date();
//        this.task_status = taskInfo_reqVO.getTask_status();
        this.task_tag = taskInfo_reqVO.getTask_tag();
        this.task_desc = taskInfo_reqVO.getTask_desc();
        this.task_priority = taskInfo_reqVO.getTask_priority();
        this.task_workload = taskInfo_reqVO.getTask_workload();
        this.checkListList = taskInfo_reqVO.getCheckListList();
    }

}
