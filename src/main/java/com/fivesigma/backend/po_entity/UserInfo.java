package com.fivesigma.backend.po_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
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
@TableName("user_info")
public class UserInfo {
    @TableId(value = "info_id", type = IdType.AUTO)
    private Integer info_id;
    @TableField(value = "user_id")
    private String user_id;
    @TableField(value = "user_p_name")
    private String user_p_name;
    @TableField(value = "user_mobile")
    private String user_mobile;
    @TableField(value = "user_sentence")
    private String user_sentence;
    @TableField(value = "user_comp")
    private String user_comp;
}
