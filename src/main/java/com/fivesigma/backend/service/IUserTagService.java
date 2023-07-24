package com.fivesigma.backend.service;

import com.fivesigma.backend.dto.UserTagDTO;
import com.fivesigma.backend.vo.AvailTagsVO;

import java.util.List;

public interface IUserTagService {
    List<UserTagDTO> getUTByUId(String user_id);
    List<UserTagDTO> getUTByTName(String tag_name);
    List<UserTagDTO> getUTByField(String field, String target);
    List<String> getTagList(List<UserTagDTO> userTagDTOS);
    List<AvailTagsVO> getAvailTag();
    boolean editUserTags(String user_id, List<String> new_tags);
}
