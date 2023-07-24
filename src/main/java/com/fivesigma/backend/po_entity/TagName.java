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
// */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tag_name")
public class TagName {
    @TableId(value = "n_tag_id", type = IdType.AUTO)
    private Integer n_tag_id;
    @TableField(value = "tag_id")
    private Integer tag_id;
    @TableField(value = "tag_name")
    private String tag_name;
}
