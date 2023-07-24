package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andy
 * @date 2022/11/15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecommendVO {
    UserInfoVO userInfoVO;
    Double avg_score;
}
