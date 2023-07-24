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
 * @date 2022/10/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("connection_table")
/**
 * connection_result: 0 -> send invitation, wait for confirmation
 *                    1 -> connection established
 */
public class Connection {
    @TableId(value = "connection_id", type = IdType.AUTO)
    private Integer connection_id;
    @TableField(value = "user_id_A")
    private String user_id_A;
    @TableField(value = "user_id_B")
    private String user_id_B;
    @TableField(value = "connection_result")
    private Integer connection_result;
    @TableField(value = "connection_msg")
    private String connection_msg;
}
