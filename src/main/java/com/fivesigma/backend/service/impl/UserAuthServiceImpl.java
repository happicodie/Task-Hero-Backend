package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fivesigma.backend.dao.IUserAuthDao;
import com.fivesigma.backend.dao.IUserInfoDao;
import com.fivesigma.backend.dto.UserAuthDTO;
import com.fivesigma.backend.dto.UserDetailDTO;
import com.fivesigma.backend.po_entity.UserAuth;
import com.fivesigma.backend.po_entity.UserInfo;
import com.fivesigma.backend.service.IUserWorkloadService;
import com.fivesigma.backend.util.EmailUtil;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.vo.TokenVO;
import com.fivesigma.backend.vo.UserAuthVO;
import com.fivesigma.backend.vo.UserRegVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Andy
 * @date 2022/10/8
 */

@Service
public class UserAuthServiceImpl extends MPJBaseServiceImpl<IUserAuthDao, UserAuth> implements UserDetailsService {

    @Autowired
    private IUserAuthDao userAuthDao;
    @Autowired
    private IUserInfoDao userInfoDao;
    @Autowired
    private IUserWorkloadService userWorkloadService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private NoOpPasswordEncoder noOpPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EmailUtil emailUtil;

    /**
     * inner func for spring security
     * @param username probably user email
     * @return user dto by given username and password if exists
     * @throws UsernameNotFoundException exception
     */
    @Override
    public UserDetailDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo: find user by username from database
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        UserAuth userAuth = userAuthDao.selectOne(queryWrapper);

        if (userAuth == null) throw new UsernameNotFoundException("user not exist");
        return new UserDetailDTO(userAuth.getUser_name(), userAuth.getUser_password(), new ArrayList<>(), userAuth.getUser_id());
    }

//    public boolean userExist(String username){
//        UserAuth userAuth = userAuthDAO.get_user(username);
//    }

    /**
     *
     * @param userRegVO personal name, username(email), password
     * @return boolean
     */
    public boolean register(UserRegVO userRegVO){

        String userFullName = userRegVO.getUser_name();  //personal name
        String username = userRegVO.getUser_email();          //email
        String rawPwd = userRegVO.getPassword();

        //todo: check if username(unique) exist
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        UserAuth userAuth = userAuthDao.selectOne(queryWrapper);
        if (userAuth == null){
            //todo: insert into user_auth
            userAuth = new UserAuth();
            userAuth.setUser_name(username);
            userAuth.setUser_password(bCryptPasswordEncoder.encode(rawPwd));
            userAuthDao.insert(userAuth);

            //todo: insert only userFullName into user_info
            String user_id = userAuth.getUser_id();
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(user_id);
            userInfo.setUser_p_name(userFullName);
            userInfoDao.insert(userInfo);

            //todo: init user available working hours
            userWorkloadService.inOrUpWorkload(user_id, 20.);
            return true;
        }else {
            return false;
        }


    }

    /**
     *
     * @param userAuthVO username(email) and password
     * @return token and user id
     */
    public TokenVO login(UserAuthVO userAuthVO){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthVO.getUser_email(), userAuthVO.getPassword()
                    ));
            UserDetailDTO userDetailDTO = this.loadUserByUsername(userAuthVO.getUser_email());
            String token = jwtUtil.generateToken(userDetailDTO);
            return new TokenVO(token, userDetailDTO.getUser_id());
        } catch (BadCredentialsException e) {
//            throw new RuntimeException("false username or password");
            return null;
        }
    }

    public UserAuthDTO getPwdByUserName(String user_name){
        MPJLambdaWrapper<UserAuth> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(UserAuth::getUser_name)
                .select(UserAuth::getUser_password)
                .eq(UserAuth::getUser_name, user_name);
        return userAuthDao.selectJoinOne(UserAuthDTO.class, wrapper);
    }

    public void forgetPwd(String user_name, String real_email){
        UserAuthDTO userAuthDTO = getPwdByUserName(user_name);
        if (userAuthDTO == null) return;
        String subject = "task hero";
        String password = userAuthDTO.getUser_password();

        UserAuthVO userAuthVO = new UserAuthVO();

        TokenVO tokenVO = null;

        String body = user_name + " --- ";
        emailUtil.send(real_email, subject, body);
    }
}
