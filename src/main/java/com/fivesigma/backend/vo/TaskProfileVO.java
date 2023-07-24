package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/11/1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProfileVO {
    private Double available_hours;
    private Double working_hours;
    private Double Busy_estimate;
    private List<TaskInfo_ResVO> taskInfo_resVOS;
}
