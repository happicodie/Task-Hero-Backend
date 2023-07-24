package com.fivesigma.backend.vo;

import com.fivesigma.backend.po_entity.Log;
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
public class TaskCreateVO {
    private String task_id;
    private List<Log> logs;
}
