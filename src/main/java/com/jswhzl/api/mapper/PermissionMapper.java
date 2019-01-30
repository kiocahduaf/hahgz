package com.jswhzl.api.mapper;

import com.jswhzl.api.entity.Permission;
import com.jswhzl.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
public interface PermissionMapper extends BaseDao<Permission> {

    /**
     * 根据roleId获取菜单列表
     *
     * @param roleId
     * @return
     */
    List<Permission> findMenu(@Param("roleId") Long roleId, @Param("parentId") Long parentId,
                              @Param("level") Integer level,
                              @Param("type") Integer type);
}
