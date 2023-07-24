package com.fivesigma.backend.vo;

import com.fivesigma.backend.po_entity.CheckList;
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
public class TaskInfo_ReqVO {
    private String task_name;
    private String assignee_id;
    private String start_date;
    private String end_date;
    private String task_status;
    private String task_tag;
    private String task_desc;
    private String task_priority;
    private Double task_workload;
    private List<CheckList> checkListList;
}
