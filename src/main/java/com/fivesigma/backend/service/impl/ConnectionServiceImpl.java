package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fivesigma.backend.dao.IConnectionDao;
import com.fivesigma.backend.po_entity.Connection;
import com.fivesigma.backend.po_entity.UserInfo;
import com.fivesigma.backend.service.IConnectionService;
import com.fivesigma.backend.service.IUserTagService;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.vo.ConnectionVO;
import com.fivesigma.backend.vo.InvitationVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 * @date 2022/10/12
 */
@Service
public class ConnectionServiceImpl extends MPJBaseServiceImpl<IConnectionDao, Connection> implements IConnectionService {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private IConnectionDao connectionDao;
    @Autowired
    private IUserTagService userTagService;

    /**
     * this mainly for finding partners based on token user_id
     * @param token from header
     * @param relation probably 1 (now only use for searching partner)
     * @return partners list
     */
    @Override
    public List<ConnectionVO> getConnectionByToken(String token, Integer relation, String target_id, boolean need_tags) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);

        MPJLambdaWrapper<Connection> wrapper1 = new MPJLambdaWrapper<>();
        wrapper1.selectAs(Connection::getUser_id_B, ConnectionVO::getUser_id)
                .selectAs(UserInfo::getUser_p_name, ConnectionVO::getUser_name)
//                .select(Connection::getConnection_result)
                .select(Connection::getConnection_msg)
                .leftJoin(UserInfo.class, UserInfo::getUser_id, Connection::getUser_id_B)
                .eq(Connection::getUser_id_A, user_id)
                .eq(Connection::getConnection_result, relation);
        if (target_id != null){
            wrapper1.eq(Connection::getUser_id_B, target_id);
        }

        List<ConnectionVO> list_A = connectionDao.selectJoinList(ConnectionVO.class, wrapper1);

        MPJLambdaWrapper<Connection> wrapper2 = new MPJLambdaWrapper<>();
        wrapper2.selectAs(Connection::getUser_id_A, ConnectionVO::getUser_id)
                .selectAs(UserInfo::getUser_p_name, ConnectionVO::getUser_name)
//                .select(Connection::getConnection_result)
                .select(Connection::getConnection_msg)
                .leftJoin(UserInfo.class, UserInfo::getUser_id, Connection::getUser_id_A)
                .eq(Connection::getUser_id_B, user_id)
                .eq(Connection::getConnection_result, relation);
        if (target_id != null){
            wrapper2.eq(Connection::getUser_id_A, target_id);
        }

        List<ConnectionVO> list_B = connectionDao.selectJoinList(ConnectionVO.class, wrapper2);

        List<ConnectionVO> connectionVOList = new ArrayList<>();
        connectionVOList.addAll(list_A);
        connectionVOList.addAll(list_B);
        if (!need_tags) return connectionVOList;
        //todo: for each VO get tags
        assignTags(connectionVOList);
        return connectionVOList;
    }

    /**
     *checking relationship type of target user
     * @param token from header
     * @param target_id target user
     * @return 0: self,
     *         1: you -> other
     *         2: other -> you
     *         3: already partner
     *         4: stranger
     */
    @Override
    public String checkRelation(String token, String target_id) {

        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        if (user_id.equals(target_id)) return "self";
        List<ConnectionVO> send = getSend(token, target_id, false);
        if (send.size() >= 1) return "connecting";
        List<ConnectionVO> receive = receiveInvitation(token, target_id, false);
        if (receive.size() >= 1) return "requested";
        List<ConnectionVO> connected = getConnectionByToken(token, 1, target_id, false);
        if (connected.size() >= 1) return "connected";
        return "stranger";
    }

    /**
     *
     * @param token from header
     * @param target_id 1: if null, return all your invitation you have sent
     *                  2: if specified, return if you have sent invitation to target id
     * @return a list of invitations you send by not been confirmed
     *         full ConnectionVO or without tags (for relation check)
     */
    @Override
    public List<ConnectionVO> getSend(String token, String target_id, boolean need_tags) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        MPJLambdaWrapper<Connection> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAs(Connection::getUser_id_B, ConnectionVO::getUser_id)
                .selectAs(UserInfo::getUser_p_name, ConnectionVO::getUser_name)
//                .select(Connection::getConnection_result)
                .select(Connection::getConnection_msg)
                .leftJoin(UserInfo.class, UserInfo::getUser_id, Connection::getUser_id_B)
                .eq(Connection::getUser_id_A, user_id)
                .eq(Connection::getConnection_result, 0);
        if (target_id != null){
            wrapper.eq(Connection::getUser_id_B, target_id);
        }
        List<ConnectionVO> connectionVOList = connectionDao.selectJoinList(ConnectionVO.class, wrapper);
        if (!need_tags) return connectionVOList;
        //todo: for each VO get tags
        assignTags(connectionVOList);
        return connectionVOList;
    }

    /**
     *
     * @param token from header
     * @param invitationVO target user and greeting message
     * @return status of sending invitation
     */
    @Override
//    @Transactional
    public String sendInvitation(String token, InvitationVO invitationVO) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        String target_id = invitationVO.getUser_id();
        //todo: can only send invitation to stranger
        String check_res = checkRelation(token, target_id);
        if (check_res.equals("self")) {
            return "can not send to yourself";
        }else if (check_res.equals("connecting")){
            return "you have send invitation to: " + target_id;
        }else if (check_res.equals("requested")){
            return "you have receive invitation from: " + target_id;
        }else if(check_res.equals("connected")){
            return "you are already partner with: " + target_id;
        }else {
            Connection connection = new Connection();
            connection.setUser_id_A(user_id);
            connection.setUser_id_B(target_id);
            connection.setConnection_result(0);
            connection.setConnection_msg(invitationVO.getConnection_msg());
            connectionDao.insert(connection);
            return "send";
        }
    }

    /**
     *
     * @param token from header
     * @param target_id 1: if null, return all your invitation you have received
     *                  2: if specified, return if you have received invitation to target id
     * @return invitations from others that you have not confirmed
     *          full ConnectionVO or without tags (for relation check)
     */
    @Override
    public List<ConnectionVO> receiveInvitation(String token, String target_id, boolean need_tags) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        MPJLambdaWrapper<Connection> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAs(Connection::getUser_id_A, ConnectionVO::getUser_id)
                .selectAs(UserInfo::getUser_p_name, ConnectionVO::getUser_name)
//                .select(Connection::getConnection_result)
                .select(Connection::getConnection_msg)
                .leftJoin(UserInfo.class, UserInfo::getUser_id, Connection::getUser_id_A)
                .eq(Connection::getUser_id_B, user_id)
                .eq(Connection::getConnection_result, 0);
        if (target_id != null){
            wrapper.eq(Connection::getUser_id_A, target_id);
        }
        List<ConnectionVO> connectionVOList = connectionDao.selectJoinList(ConnectionVO.class, wrapper);
        if (!need_tags) return connectionVOList;
        //todo: for each VO get tags
        assignTags(connectionVOList);
        return connectionVOList;
    }

    /**
     * confirm invitation
     * @param token from header
     * @param target_id other s id
     * @return
     */
    @Override
    public int acceptInvitation(String token, String target_id) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        LambdaUpdateWrapper<Connection> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Connection::getConnection_result, 1)
                .eq(Connection::getUser_id_B, user_id)
                .eq(Connection::getUser_id_A, target_id);
        return connectionDao.update(null, updateWrapper);
    }

    /**
     * inner func for assigning tags
     * @param connectionVOList vo list
     */
    @Override
    public void assignTags(List<ConnectionVO> connectionVOList) {
        for (ConnectionVO connectionVO : connectionVOList){
            List<String> tags = userTagService.getTagList(
                    userTagService.getUTByUId(connectionVO.getUser_id()));
            connectionVO.setTags(tags);
        }
    }

    /**
     *can use for reject invitation or stop connection
     * @param token from header
     * @param target_id target user
     * @return no return
     */
    @Override
    public int deleteConnection(String token, String target_id) {
        token = token.replace("Bearer ", "");
        String user_id = jwtUtil.userIdInToken(token);
        QueryWrapper<Connection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id_A", user_id)
                .eq("user_id_B", target_id)
                .or(QueryWrapper -> QueryWrapper.eq("user_id_A", target_id)
                                                .eq("user_id_B", user_id));
        return connectionDao.delete(wrapper);
    }
}


