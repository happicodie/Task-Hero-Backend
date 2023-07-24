package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailTagsVO {
    private Integer tag_id;
    private String tag_name;
}
