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
 * @date 2022/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_workload")
public class UserWorkload {
    @TableId(value = "sys_id", type = IdType.AUTO)
    Integer sys_id;
    @TableField(value = "user_id")
    String user_id;
    @TableField(value = "user_available")
    Double user_available;
    @TableField(value = "user_working")
    Double user_working;
    @TableField(value = "user_busy")
    Double user_busy;
}
