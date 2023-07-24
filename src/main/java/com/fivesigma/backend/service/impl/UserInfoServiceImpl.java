package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fivesigma.backend.dao.IUserImageDao;
import com.fivesigma.backend.dao.IUserInfoDao;
import com.fivesigma.backend.dto.UserBasicInfoDTO;
import com.fivesigma.backend.dto.UserTagDTO;
import com.fivesigma.backend.dto.UserWorkloadDTO;
import com.fivesigma.backend.po_entity.UserAuth;
import com.fivesigma.backend.po_entity.UserImage;
import com.fivesigma.backend.po_entity.UserInfo;
import com.fivesigma.backend.service.*;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.vo.*;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andy
 * @date 2022/10/12
 */

@Service
public class UserInfoServiceImpl extends MPJBaseServiceImpl<IUserInfoDao, UserInfo> implements IUserInfoService {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private IUserInfoDao userInfoDao;
    @Autowired
    private IUserTagService userTagService;
    @Autowired
    private IUserImageDao userImageDao;
    @Autowired
    private IConnectionService connectionService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private IUserWorkloadService userWorkloadService;

    /**
     * top level func: basic info + tags + image + relation + (task things)
     * @param token from header
     * @return return your own profile
     */
    @Override
//    @Transactional
    public UserInfoVO getInfoByToken(String token) {

//        token = token.replace("Bearer ", "");
//        String user_id = jwtUtil.userIdInToken(token);
//        List<UserInfoVO> infoVOS = getInfoFull(new String[]{"user_id"}, user_id);
//        UserInfoVO res = infoVOS.get(0);
//        res.setRelation("self");
//        res.replaceNull();
        String user_id = jwtUtil.tryGetId(token);
        UserInfoVO res = getInfoByUserId(token, user_id, true);
        return res;
    }

    @Override
    public UserInfoVO getInfoByUsername(String username) {
        return null;
    }

    @Override
    public UserInfoVO getInfoByUserId(String token, String target_id, boolean needTask) {
        List<UserInfoVO> infoVOS = getInfoFull(new String[]{"user_id"}, target_id);
        if (infoVOS.size() > 0){
            UserInfoVO userInfoVO = infoVOS.get(0);
            userInfoVO.setRelation(connectionService.checkRelation(token, target_id));

            List<ConnectionVO> connectionVOS = connectionService.getConnectionByToken(token, 0, target_id, false);
            if (connectionVOS.size() >= 1){
                String connection_msg = connectionVOS.get(0).getConnection_msg();
                userInfoVO.setConnection_message(connection_msg);
            }
            //todo: get tasks things
            if (!needTask) return userInfoVO;
            String relation = userInfoVO.getRelation();
            if (relation == "connected" || relation == "self"){
                TaskProfileVO taskProfileVO = new TaskProfileVO();
                taskProfileVO.setTaskInfo_resVOS(taskService.getTask(token, 0, target_id));
                //todo: get workload things
                UserWorkloadDTO workloadDTO = userWorkloadService.getWorkloadById(userInfoVO.getUser_id());
                taskProfileVO.setAvailable_hours(workloadDTO.getUser_available());
                taskProfileVO.setWorking_hours(workloadDTO.getUser_working());
                taskProfileVO.setBusy_estimate(workloadDTO.getUser_busy());
                userInfoVO.setTaskProfileVO(taskProfileVO);
            }
            userInfoVO.replaceNull();
            return userInfoVO;
        }else {
            return null;
        }

    }

    /**
     * top level func
     * related to several tables: user_info, user_image, user_tag, user_name
     * @param token from header
     * @param editProfileVO latest files of user info
     * @return no return
     */
    @Override
//    @Transactional
    public int editInfoByToken(String token, EditProfileVO editProfileVO) {

        //todo: edit your own profile
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);

        //todo: 1. update basic info
        LambdaUpdateWrapper<UserInfo> info_wrapper = new LambdaUpdateWrapper<>();
        info_wrapper.set(UserInfo::getUser_p_name, editProfileVO.getUser_name())
                .set(UserInfo::getUser_mobile, editProfileVO.getUser_mobile())
                .set(UserInfo::getUser_sentence, editProfileVO.getUser_sentence())
                .set(UserInfo::getUser_comp, editProfileVO.getUser_comp())
                .eq(UserInfo::getUser_id, user_id);
        userInfoDao.update(null, info_wrapper);
        //todo: 2. update user image
        LambdaUpdateWrapper<UserImage> image_wrapper = new LambdaUpdateWrapper<>();
        image_wrapper.set(UserImage::getImage, editProfileVO.getUser_image())
                .eq(UserImage::getUser_id, user_id);
        if(userImageDao.update(null, image_wrapper) == 0){
            userImageDao.insert(new UserImage(null, user_id, editProfileVO.getUser_image()));
        }
//        userImageDao.update(null, image_wrapper);
        //todo: 3. update tags
        List<String> new_tags = editProfileVO.getTags();
        userTagService.editUserTags(user_id, new_tags);

        return 0;
    }

    @Override
    public boolean editInfoById(Integer id) {
        return false;
    }

    /**
     * search user info by choosing from any combination of (id, name, email)
     * @param fieldList fields you want to search,
     * @param target field value
     * @return basic user info, not included tags, image, relation, or (task things)
     */
    @Override
    public List<UserBasicInfoDTO> getBInfoByFiled(String[] fieldList, String target) {

        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(UserInfo::getUser_id)
                .selectAs(UserAuth::getUser_name, UserBasicInfoDTO::getUser_email)
                .select(UserInfo::getUser_p_name)
                .select(UserInfo::getUser_mobile)
                .select(UserInfo::getUser_sentence)
                .select(UserInfo::getUser_comp)
                .leftJoin(UserAuth.class, UserAuth::getUser_id, UserInfo::getUser_id);
        for (String field : fieldList){
            switch (field) {
                case "user_id":
                    wrapper.eq(UserAuth::getUser_id, target);
                    break;
                case "user_name":
                    wrapper.or().like(UserInfo::getUser_p_name, target);
                    break;
                case "user_email":
                    wrapper.or().eq(UserAuth::getUser_name, target);
                    break;
                case "user_comp":
                    wrapper.or().like(UserInfo::getUser_comp, target);
            }
        }
        return userInfoDao.selectJoinList(UserBasicInfoDTO.class, wrapper);
    }

    @Override
    public List<UserBasicInfoDTO> getBInfoByFiledPrecise(UserSearchVO userSearchVO) {
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(UserInfo::getUser_id)
                .selectAs(UserAuth::getUser_name, UserBasicInfoDTO::getUser_email)
                .select(UserInfo::getUser_p_name)
                .select(UserInfo::getUser_mobile)
                .select(UserInfo::getUser_sentence)
                .select(UserInfo::getUser_comp)
                .leftJoin(UserAuth.class, UserAuth::getUser_id, UserInfo::getUser_id);

        String user_id;
        String user_name;
        String user_comp;
        String user_email;
//        List<String> tags;
        if ((user_id = userSearchVO.getUser_id()) != null){
            wrapper.eq(UserAuth::getUser_id, user_id);
        }
        if ((user_name = userSearchVO.getUser_name()) != null){
            wrapper.and(MPJLambdaWrapper-> MPJLambdaWrapper.like(UserInfo::getUser_p_name, user_name));
        }
        if ((user_comp = userSearchVO.getUser_comp()) != null){
            wrapper.and(MPJLambdaWrapper-> MPJLambdaWrapper.like(UserInfo::getUser_comp, user_comp));
        }
        if ((user_email = userSearchVO.getUser_email()) != null){
            wrapper.and(MPJLambdaWrapper -> MPJLambdaWrapper.eq(UserAuth::getUser_name, user_email));
        }
        return userInfoDao.selectJoinList(UserBasicInfoDTO.class, wrapper);
    }

    /**
     *
     * @param fieldList for getBInfoByFiled func
     * @param target for getBInfoByFiled func
     * @return
     */
    @Override
//    @Transactional
    public List<UserInfoVO> getInfoFull(String[] fieldList, String target) {
        List<UserBasicInfoDTO> basicInfoDTOS = getBInfoByFiled(fieldList, target);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        //todo: foreach basic info search corresponding tags and image
//        if (basicInfoDTOS == null) return
        for (UserBasicInfoDTO dto : basicInfoDTOS){
            UserInfoVO infoVO = new UserInfoVO(dto);

            QueryWrapper<UserImage> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", dto.getUser_id());
            UserImage  userImage = userImageDao.selectOne(wrapper);
//            String image = userImageDao.selectOne(wrapper).getImage();    //null point exception
            if (userImage != null){
                infoVO.setUser_image(userImage.getImage());
            }

            List<UserTagDTO> userTagDTOS = userTagService.getUTByUId(dto.getUser_id());
            infoVO.setTags(userTagService.getTagList(userTagDTOS));

            userInfoVOList.add(infoVO);
        }
        return userInfoVOList;
    }

    @Override
    public List<UserInfoVO> getInfoFullPrecise(UserSearchVO userSearchVO) {
        List<UserBasicInfoDTO> basicInfoDTOS = getBInfoByFiledPrecise(userSearchVO);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();

        for (UserBasicInfoDTO dto : basicInfoDTOS){
            UserInfoVO infoVO = new UserInfoVO(dto);

            QueryWrapper<UserImage> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", dto.getUser_id());
            UserImage  userImage = userImageDao.selectOne(wrapper);
//            String image = userImageDao.selectOne(wrapper).getImage();    //null point exception
            if (userImage != null){
                infoVO.setUser_image(userImage.getImage());
            }

            List<UserTagDTO> userTagDTOS = userTagService.getUTByUId(dto.getUser_id());
            infoVO.setTags(userTagService.getTagList(userTagDTOS));

            userInfoVOList.add(infoVO);
        }

        List<String> search_tags = userSearchVO.getTags();
        if (search_tags == null) return userInfoVOList;

        //todo: check tags array
        Iterator<UserInfoVO> iterator = userInfoVOList.listIterator();
        while (iterator.hasNext()){
            UserInfoVO info = iterator.next();
            List<String> user_tags = new ArrayList<>(info.getTags());
//            List<String> search_tags = new ArrayList<>(userSearchVO.getTags());
            user_tags.retainAll(search_tags);
            //if two list have no intersection, drop this user
            if (user_tags.size() == 0){
                iterator.remove();
            }
        }
        return userInfoVOList;
    }
}
