package com.fivesigma.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileVO {
//    private String user_email;
    private String user_name;
    private String user_mobile;
    private String user_image;
    private List<String> tags;
    private String user_sentence;
    protected String user_comp;
}
