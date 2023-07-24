package com.fivesigma.backend.service;

import com.fivesigma.backend.dto.UserBasicInfoDTO;
import com.fivesigma.backend.vo.EditProfileVO;
import com.fivesigma.backend.vo.UserInfoVO;
import com.fivesigma.backend.vo.UserSearchVO;

import java.util.List;

public interface IUserInfoService {

    UserInfoVO getInfoByToken(String token);
    UserInfoVO getInfoByUsername(String username);
    UserInfoVO getInfoByUserId(String token, String target_id, boolean needTask);
    int editInfoByToken(String token, EditProfileVO editProfileVO);
    boolean editInfoById(Integer id);
    List<UserBasicInfoDTO> getBInfoByFiled(String[] fieldList, String target);
    List<UserBasicInfoDTO> getBInfoByFiledPrecise(UserSearchVO userSearchVO);
    List<UserInfoVO> getInfoFull(String[] fieldList, String target);
    List<UserInfoVO> getInfoFullPrecise(UserSearchVO userSearchVO);
}
