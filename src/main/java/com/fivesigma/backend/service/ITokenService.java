package com.fivesigma.backend.service;

import com.fivesigma.backend.po_entity.TokenBlackList;

public interface ITokenService {
    TokenBlackList getBlackToken(String token);
    boolean inBlackList(String token);
}
