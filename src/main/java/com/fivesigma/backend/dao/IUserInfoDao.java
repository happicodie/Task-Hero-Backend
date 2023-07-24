package com.fivesigma.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fivesigma.backend.po_entity.UserInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IUserInfoDao extends MPJBaseMapper<UserInfo>{
}
