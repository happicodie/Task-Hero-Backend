package com.fivesigma.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/29
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRelDTO {
    private String task_id;
    private String assigner_id;
    private String assignee_id;
    private boolean accept;

    public boolean getAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
