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
@TableName("user_tag")
public class UserTag {
    @TableId(value = "u_tag_id", type = IdType.AUTO)
    private Integer u_tag_id;
    @TableField(value = "user_id")
    private String user_id;
    @TableField(value = "tag_id")
    private Integer tag_id;
//    @TableField(value = "tag_name")
//    private String tag_name;
    public UserTag(String user_id, Integer tag_id){
        this.user_id = user_id;
        this.tag_id = tag_id;
    }
}
