package com.jswhzl.api.mapper;

import com.jswhzl.api.entity.Role;
import com.jswhzl.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface RoleMapper extends BaseDao<Role> {

    /**
     * 根据userId获取角色列表
     *
     * @param userId 用户ID
     * @return
     */
    Role findByUserId(@Param("userId") Long userId);
}
