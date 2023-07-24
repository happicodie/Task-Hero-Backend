package com.fivesigma.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTagDTO {
    private String user_id;
    private Integer tag_id;
    private String tag_name;
}
