package com.fivesigma.backend.service;

import com.fivesigma.backend.dto.UserWorkloadDTO;

public interface IUserWorkloadService {
    boolean inOrUpWorkload(String token, Double user_available);
    UserWorkloadDTO getWorkloadById(String target_id);
    UserWorkloadDTO getWorkloadByToken(String token);
}
