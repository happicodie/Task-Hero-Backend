package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/12
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionVO {
    private String user_id;    //others id
//    private String user_email;  //others email
    private String user_name;   //others fullName
    private List<String> tags;
//    private Integer connection_result;  //no use
    private String connection_msg;
}
