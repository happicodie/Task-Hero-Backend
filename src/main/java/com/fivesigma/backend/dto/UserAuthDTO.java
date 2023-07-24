package com.fivesigma.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/11/16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {
    private String user_name;
    private String user_password;
}
