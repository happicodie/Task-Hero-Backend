package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fivesigma.backend.dao.ITagNameDao;
import com.fivesigma.backend.dao.IUserTagDao;
import com.fivesigma.backend.dto.UserTagDTO;
import com.fivesigma.backend.po_entity.TagName;
import com.fivesigma.backend.po_entity.UserTag;
import com.fivesigma.backend.service.IUserTagService;
import com.fivesigma.backend.vo.AvailTagsVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy
 * @date 2022/10/15
 */
@Service
public class UserTagServiceImpl extends MPJBaseServiceImpl<IUserTagDao, UserTag> implements IUserTagService {

    @Autowired
    private IUserTagDao userTagDao;
    @Autowired
    private ITagNameDao tagNameDao;

    /**
     * top level func
     * @param user_id user id
     * @return UserTagDTO list
     */
    @Override
    public List<UserTagDTO> getUTByUId(String user_id) {
        return getUTByField("user_id", user_id);
    }

    /**
     * top level func
     * @param tag_name tag name
     * @return UserTagDTO list
     */
    @Override
    public List<UserTagDTO> getUTByTName(String tag_name) {
        return getUTByField("tag_name", tag_name);
    }

    /**
     *inner func
     * @param field user_id or tag_name
     * @param target same
     * @return UserTagDTO list
     */
    @Override
    public List<UserTagDTO> getUTByField(String field, String target) {
        MPJLambdaWrapper<UserTag> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(UserTag::getUser_id)
                .select(UserTag::getU_tag_id)
                .select(TagName::getTag_name)
                .leftJoin(TagName.class, TagName::getTag_id, UserTag::getTag_id);
        if (field.equals("user_id")){
            wrapper.eq(UserTag::getUser_id, target);
        }else {
            wrapper.eq(TagName::getTag_name, target);
        }
        return userTagDao.selectJoinList(UserTagDTO.class, wrapper);
    }

    /**
     * convert UserTagDTO list into pure tag name array
     * @param userTagDTOS UserTagDTO list
     * @return tag name array
     */
    public List<String> getTagList(List<UserTagDTO> userTagDTOS){
        List<String> res = new ArrayList<>();
        for (UserTagDTO dto : userTagDTOS){
            res.add(dto.getTag_name());
        }
        return res;
    }

    /**
     * get available tags for choosing
     * @return AvailTagsVO list
     */
    @Override
    public List<AvailTagsVO> getAvailTag() {
        MPJLambdaWrapper<TagName> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(TagName::getTag_id)
                .select(TagName::getTag_name);
        return tagNameDao.selectJoinList(AvailTagsVO.class, wrapper);
    }

    /**
     * edit your own tags (inner func for info edition)
     * @param user_id your user id
     * @param new_tags new tag list (should be tag id in Integer!!)
     * @return no returns
     */
    @Override
    public boolean editUserTags(String user_id, List<String> new_tags) {
        //todo: first delete all tags of target user
        QueryWrapper<UserTag> delWrapper = new QueryWrapper<>();
        delWrapper.eq("user_id", user_id);
        userTagDao.delete(delWrapper);

        List<AvailTagsVO> availTagsVOS = getAvailTag();
        Map<String, Integer> tagNameIds = new HashMap<>();
        for (AvailTagsVO tagsVO : availTagsVOS){
            tagNameIds.put(tagsVO.getTag_name(), tagsVO.getTag_id());
        }

        //todo: then insert new tags
        List<UserTag> userTagList = new ArrayList<>();
        for (String tag_name : new_tags){
            userTagList.add(new UserTag(user_id, tagNameIds.get(tag_name)));
        }
        return this.saveBatch(userTagList);
    }


}
