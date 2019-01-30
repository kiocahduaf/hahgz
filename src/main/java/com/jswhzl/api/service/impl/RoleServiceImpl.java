package com.jswhzl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jswhzl.api.entity.Role;
import com.jswhzl.api.entity.RolePermission;
import com.jswhzl.api.mapper.RoleMapper;
import com.jswhzl.api.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    private static final long serialVersionUID = -4361096693526226917L;
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermissionServiceImpl rolePermissionService;

    @Override
    public Role findByUserId(Long userId) {
        return roleMapper.findByUserId(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean grant(Long roleId, List<Long> perIds) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("roleId", roleId);
        if (rolePermissionService.remove(wrapper)) {
            if (null != perIds && perIds.size() > 0) {
                List<RolePermission> saveList = new ArrayList<>();
                perIds.forEach(perId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setPerId(perId);
                    rolePermission.setRoleId(roleId);
                    saveList.add(rolePermission);
                });
                if (!rolePermissionService.saveBatch(saveList)){
                    throw new RuntimeException("授权失败");
                }
            }
            return true;
        }else{
            throw new RuntimeException("授权失败");
        }
    }
}
