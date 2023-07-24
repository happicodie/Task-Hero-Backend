package com.fivesigma.backend.dao;

import com.fivesigma.backend.po_entity.UserTag;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IUserTagDao extends MPJBaseMapper<UserTag> {
}
