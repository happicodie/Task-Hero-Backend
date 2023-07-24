package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/10/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationVO {
    private String user_id;
    private String connection_msg;
}
