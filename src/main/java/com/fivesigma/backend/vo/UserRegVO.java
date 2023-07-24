package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegVO extends UserAuthVO{
    private String user_name;    //name for user_info: user_name
//    private String username;    //email address
//    private String password;
}
