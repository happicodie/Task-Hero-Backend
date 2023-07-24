package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/11/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchVO {
    private List<String> tags;
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_comp;
}
