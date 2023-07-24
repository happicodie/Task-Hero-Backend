package com.fivesigma.backend.vo;

import com.fivesigma.backend.dto.UserBasicInfoDTO;
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
public class UserInfoVO {
    private String user_id;         //UserBasicInfoDTO
    private String user_email;      //UserBasicInfoDTO
    private String user_name;     //UserBasicInfoDTO

    private String user_image;      //user_image table !!!!

    private String user_mobile;     //UserBasicInfoDTO

    private String user_sentence;   //UserBasicInfoDTO

    private String user_comp;       //UserBasicInfoDTO
    private List<String> tags;
    private String relation;   //0:self, 1:you send request, 2:other send request to you, 3:connected
    private String connection_message;
    //todo: task things
    private TaskProfileVO taskProfileVO;

    public UserInfoVO(UserBasicInfoDTO userBasicInfoDTO){
        this.user_id = userBasicInfoDTO.getUser_id();
        this.user_email = userBasicInfoDTO.getUser_email();
        this.user_name = userBasicInfoDTO.getUser_p_name();
        this.user_mobile = userBasicInfoDTO.getUser_mobile();
        this.user_sentence = userBasicInfoDTO.getUser_sentence();
        this.user_comp = userBasicInfoDTO.getUser_comp();
    }
    public void replaceNull(){
        this.setUser_image(this.getUser_image() == null ? "" : this.getUser_image());
        this.setUser_mobile(this.getUser_mobile() == null ? "" : this.getUser_mobile());
        this.setUser_sentence(this.getUser_sentence() == null ? "" : this.getUser_sentence());
        this.setUser_comp(this.getUser_comp() == null ? "" : this.getUser_comp());
        this.setConnection_message(this.getConnection_message() == null ? "" : this.getConnection_message());
    }
}
