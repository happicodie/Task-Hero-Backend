package com.fivesigma.backend.service;

import com.fivesigma.backend.po_entity.Connection;
import com.fivesigma.backend.vo.ConnectionVO;
import com.fivesigma.backend.vo.InvitationVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface IConnectionService {

    List<ConnectionVO> getConnectionByToken(String token, Integer relation, String target_id, boolean need_tags);
    String checkRelation(String token, String user_id_B);
    List<ConnectionVO> getSend(String token, String user_id_B, boolean need_tags);
    String sendInvitation(String token, InvitationVO invitationVO);
    List<ConnectionVO> receiveInvitation(String token, String user_id_B, boolean need_tags);
    int acceptInvitation(String token, String target_id);
    void assignTags(List<ConnectionVO> connectionVOList);
    int deleteConnection(String token, String target_id);
}
