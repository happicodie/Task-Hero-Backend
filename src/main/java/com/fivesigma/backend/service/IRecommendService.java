package com.fivesigma.backend.service;

import com.fivesigma.backend.vo.UserRecommendVO;

import java.util.List;

public interface IRecommendService {

    List<UserRecommendVO> topOnTask(String token, String tag, Integer top_count);
    List<UserRecommendVO> topOnUser();
}
