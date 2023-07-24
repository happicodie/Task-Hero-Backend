package com.fivesigma.backend.po_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("token_auth")
public class TokenBlackList {
    @TableId(value = "token_id", type = IdType.AUTO)
    private Integer token_id;
    @TableField(value = "token")
    private String token;
}
