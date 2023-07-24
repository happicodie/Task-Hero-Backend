package com.fivesigma.backend.controller;

import com.fivesigma.backend.service.IConnectionService;
import com.fivesigma.backend.util.ResponseUtil;
import com.fivesigma.backend.vo.ConnectionVO;
import com.fivesigma.backend.vo.InvitationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Andy
 * @date 2022/10/17
 */

@Api
@RestController
@RequestMapping("/connection")
public class UserConnectionController {

    @Autowired
    private IConnectionService connectionService;


    @ApiOperation("get user connected")
    @GetMapping("/connected")
    public ResponseUtil<List<ConnectionVO>> getConnectedByToken(@ApiIgnore @RequestHeader("Authorization") String token){
        //todo: find your partner (connection result = 1)
        List<ConnectionVO> connectionVOList = null;
        try {
            connectionVOList = connectionService.getConnectionByToken(token, 1, null, true);
            return ResponseUtil.ok("get partners success", connectionVOList);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", connectionVOList);
        }
    }

    @ApiOperation("get user connecting")
    @GetMapping("/sendlist")
    public ResponseUtil<List<ConnectionVO>> getSend(@ApiIgnore @RequestHeader("Authorization") String token){
        List<ConnectionVO> connectionVOList = null;
        try {
            connectionVOList = connectionService.getSend(token, null, true);
            return ResponseUtil.ok("get sends success", connectionVOList);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", connectionVOList);
        }
    }

    @ApiOperation("get user requested")
    @GetMapping("/receivelist")
    public ResponseUtil<List<ConnectionVO>> getReceive(@ApiIgnore @RequestHeader("Authorization") String token){
        List<ConnectionVO> connectionVOList = null;
        try {
            connectionVOList = connectionService.receiveInvitation(token, null, true);
            return ResponseUtil.ok("get receives success", connectionVOList);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", connectionVOList);
        }
    }

    @ApiOperation("send invitation")
    @PostMapping("/send")
    public ResponseUtil<String> sendInvitation(@ApiIgnore @RequestHeader("Authorization") String token,
                                               @RequestBody InvitationVO invitationVO){
        try {
            String send_status = connectionService.sendInvitation(token, invitationVO);
            if (send_status.equals("send")){
                return ResponseUtil.ok(send_status, null);
            }else {
                return ResponseUtil.forbidden(send_status, null);
            }
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", null);
        }
    }

    @ApiOperation("accept invitation")
    @PostMapping("/accept/{id}")
    public ResponseUtil<String> accept(@ApiIgnore @RequestHeader("Authorization") String token,
                                       @PathVariable(name = "id") String target_id){
        try {
            Integer res = connectionService.acceptInvitation(token, target_id);
            return ResponseUtil.ok("accept", null);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", null);
        }
    }


    @ApiOperation("reject invitation or stop connection")
    @PostMapping("/decline/{id}")
    public ResponseUtil<String> deleteConnection(@ApiIgnore @RequestHeader("Authorization") String token,
                                                 @PathVariable(name = "id") String target_id){
        try {
            Integer res = connectionService.deleteConnection(token, target_id);
            return ResponseUtil.ok("get receives success", null);
        } catch (Exception e) {
            return ResponseUtil.forbidden("fail", null);
        }
    }
}
