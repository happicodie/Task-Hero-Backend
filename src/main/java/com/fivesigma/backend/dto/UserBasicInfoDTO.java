package com.fivesigma.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoDTO {
    private String user_id;
    private String user_email;
    private String user_p_name;
    private String user_mobile;
    private String user_sentence;
    private String user_comp;
}
