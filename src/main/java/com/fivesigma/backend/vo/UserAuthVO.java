package com.fivesigma.backend.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthVO {
//    private String username;
    @ApiModelProperty(example = "andy_email")
    private String user_email;
    @ApiModelProperty(example = "123456")
    private String password;
}
