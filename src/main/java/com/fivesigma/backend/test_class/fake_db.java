package com.fivesigma.backend.test_class;

import com.fivesigma.backend.po_entity.UserAuth;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Andy
 * @date 2022/10/7
 */

@Component
@Data
public class fake_db {

//     private HashMap<String, String> user_db;
//
//    public fake_db(){
//        user_db = new HashMap<>();
////        user_db.put("Andy", "123456");
////        user_db.put("Tom", "123456");
////        user_db.put("Jerry", "123456");
//    }
//
//    public void register(String account, String pwd){
//        user_db.put(account, pwd);
//        this.show_db();
//    }
//
//    public void show_db(){
//        System.out.println(user_db.toString());
//    }
//
//    public UserAuth get_user(String username){
//        String pwd = user_db.get(username);
//        if (pwd == null ) return null;
//        return new UserAuth(1, username, pwd);
//    }
}

