package com.jswhzl.api.service;

import com.jswhzl.api.entity.Permission;
import com.jswhzl.common.base.BaseService;
import com.jswhzl.common.vo.MenuVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface IPermissionService extends BaseService<Permission> {

    /**
     * 根据userId获取菜单列表
     *
     * @param userId 用户ID
     * @return
     */
    List<MenuVO> findMenuByUserId(Long userId);

    /**
     * 根据userId获取权限列表
     *
     * @param userId
     * @return
     */
    List<Permission> findListByUserId(Long userId);

    /**
     * 根据roleId获取权限列表
     * @param roleId
     * @return
     */
    List<Permission> findListByRoleId(Long roleId);
}
