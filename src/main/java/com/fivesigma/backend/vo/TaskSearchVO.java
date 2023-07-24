package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/11/9
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchVO {
    String task_id;
    String task_name;
    String task_desc;
    String end_date;
    String task_status;
    String task_tag;
}
