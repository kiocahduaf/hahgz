package com.jswhzl.api.service;

import com.jswhzl.api.entity.Role;
import com.jswhzl.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface IRoleService extends BaseService<Role> {
    /**
     * 根据userId获取角色列表
     *
     * @param userId 用户ID
     * @return
     */
    Role findByUserId(@Param("userId") Long userId);

    /**
     * 授权
     *
     * @param roleId
     * @param perIds
     * @return
     */
    boolean grant(Long roleId, List<Long> perIds);
}
