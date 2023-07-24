package com.fivesigma.backend.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class UserInfoDTO {
    private String user_id;
    private String user_p_name;
    private String user_mobile;
    private String user_sentence;
    private String user_comp;
}
