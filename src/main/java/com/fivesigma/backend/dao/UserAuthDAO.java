package com.fivesigma.backend.dao;

import com.fivesigma.backend.po_entity.UserAuth;
import com.fivesigma.backend.test_class.fake_db;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Andy
 * @date 2022/10/8
 */

@Repository
public class UserAuthDAO {

//    @Autowired
//    private fake_db db;
//
//    public UserAuth get_user(String username){
//        return db.get_user(username);
//    }
//
//    public UserAuth register(UserAuth userAuth){
//        db.register(userAuth.getUser_name(), userAuth.getUser_pwd());
//        return userAuth;
//    }
}
