package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fivesigma.backend.dao.ITokenDAO;
import com.fivesigma.backend.po_entity.TokenBlackList;
import com.fivesigma.backend.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andy
 * @date 2022/10/11
 */

@Service
public class TokenServiceImpl extends ServiceImpl<ITokenDAO, TokenBlackList> implements ITokenService {

    @Autowired
    private ITokenDAO tokenDAO;

    @Override
    public TokenBlackList getBlackToken(String token) {
        TokenBlackList resToken = null;
        QueryWrapper<TokenBlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        resToken = tokenDAO.selectOne(queryWrapper);
        return resToken;
    }

    @Override
    public boolean inBlackList(String token) {
        TokenBlackList resToken = getBlackToken(token);
        return resToken == null;
    }


}
