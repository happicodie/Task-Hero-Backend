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
 * @date 2022/10/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_image")
public class UserImage {
    @TableId(value = "image_id", type = IdType.AUTO)
    private Integer image_id;
    @TableField(value = "user_id")
    private String user_id;
    @TableField(value = "image")
    private String image;
}
