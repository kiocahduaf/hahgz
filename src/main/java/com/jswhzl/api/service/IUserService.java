package com.jswhzl.api.service;

import com.jswhzl.api.entity.User;
import com.jswhzl.common.base.BaseService;
import com.jswhzl.common.dto.UserDTO;
import com.jswhzl.common.vo.ExpertUserVO;
import com.jswhzl.common.vo.UserVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface IUserService extends BaseService<User> {

    /**
     * 详情
     *
     * @param userId
     * @return
     */
    UserVO findDetailByUserId(Long userId);

    /**
     * 新增
     *
     * @param user
     * @return
     */
    boolean addUser(UserDTO user);

    /**
     * 修改
     *
     * @param user
     * @return
     */
    boolean updateUser(UserDTO user);

    /**
     * 根据depId查询用户列表
     *
     * @param depId 部门ID
     * @return
     */
    List<User> findUserListByDepId(Long depId);

    /**
     * 微信登录
     *
     * @param user
     * @return
     */
    User wxLogin(UserDTO user);
}
