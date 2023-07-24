package com.fivesigma.backend.po_entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("task_rel_table")
public class TaskRelation {
    @TableId(value = "sys_id", type = IdType.AUTO)
    private Integer sys_id;
    @TableField(value = "task_id")
    private String task_id;
    @TableField(value = "assigner_id")
    private String assigner_id;
    @TableField(value = "assignee_id")
    private String assignee_id;
    @TableField(value = "accept")
    private boolean accept;

    public boolean getAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
