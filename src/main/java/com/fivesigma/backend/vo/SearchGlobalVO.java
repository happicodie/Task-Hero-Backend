package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/11/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchGlobalVO {
    List<UserInfoVO> userInfoVOS;
    List<TaskInfo_ResVO> taskInfosToOther;
    List<TaskInfo_ResVO> taskInfosToMe;
    List<TaskInfo_ResVO> taskInfosToMyself;
}
