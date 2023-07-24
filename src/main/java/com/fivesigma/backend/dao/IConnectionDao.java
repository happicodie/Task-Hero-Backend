package com.fivesigma.backend.dao;

import com.fivesigma.backend.po_entity.Connection;
import com.fivesigma.backend.vo.ConnectionVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IConnectionDao extends MPJBaseMapper<Connection> {

    @Select("<script>" +
            " select connection.#{id_B} ,auth_2.user_email, info.user_name, connection.connection_result, connection.connection_msg" +
            " from user_auth auth_1 " +
            " join connection_table connection on connection.#{id_A} = auth_1.user_id" +
            " join user_info info on info.user_id = connection.#{id_B}" +
            " join user_auth auth_2 on auth_2.user_id = info.user_id" +
            " where auth_1.user_email = #{you}" +
            " </script>")
    List<ConnectionVO> getConnected(@Param("id_A") String id_A,
                                    @Param("id_B") String id_B,
                                    @Param("you") String you);
}
