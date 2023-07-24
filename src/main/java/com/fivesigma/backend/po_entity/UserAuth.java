package com.fivesigma.backend.po_entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("user_auth")
public class UserAuth {
    @TableId(value = "auth_id", type = IdType.AUTO)
    private Integer auth_id;
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private String user_id;    //business id random generate code
    @TableField(value = "user_name")
    private String user_name;
    @TableField(value = "user_password")
    private String user_password;
}
