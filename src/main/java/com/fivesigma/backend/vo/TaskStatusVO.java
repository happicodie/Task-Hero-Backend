package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/30
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusVO {
    private String task_id;
    private String task_status;
}
