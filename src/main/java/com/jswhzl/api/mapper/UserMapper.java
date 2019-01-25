package com.jswhzl.api.mapper;

import com.jswhzl.api.entity.User;
import com.jswhzl.common.base.BaseDao;
import com.jswhzl.common.vo.ExpertUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface UserMapper extends BaseDao<User> {

    /**
     * 根据depId查询用户列表
     *
     * @param depId 部门ID
     * @return
     */
    List<User> findUserListByDepId(@Param("depId") Long depId);

    /**
     * 根据houseId获取专家组成员列表
     *
     * @param houseId
     * @return
     */
    List<ExpertUserVO> findExpertUserListByHouseId(@Param("houseId") Long houseId);
}
