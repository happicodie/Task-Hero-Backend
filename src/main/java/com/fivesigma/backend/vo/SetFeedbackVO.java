package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/11/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetFeedbackVO {
    private String task_id;
    private Integer task_score;
    private String task_feedback;
}
