package com.fivesigma.backend.dto;

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
public class UserWorkloadDTO {
    String user_id;
    Double user_available;
    Double user_working;
    Double user_busy;
}
