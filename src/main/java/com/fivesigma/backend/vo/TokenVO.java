package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Andy
 * @date 2022/10/11
 */
@Data
@AllArgsConstructor
public class TokenVO {
    private String token;
    private String user_id;

}
