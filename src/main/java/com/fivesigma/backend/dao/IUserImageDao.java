package com.fivesigma.backend.dao;

import com.fivesigma.backend.po_entity.UserImage;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IUserImageDao extends MPJBaseMapper<UserImage> {
    //todo: no independent service, merge into userinfo service
}

