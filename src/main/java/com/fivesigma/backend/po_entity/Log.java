package com.fivesigma.backend.po_entity;

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
//@TableName("log_table")
public class Log {
//    private String timestamp;
    private String content;

}
