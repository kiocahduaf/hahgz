package com.jswhzl.api.service.impl;

import com.jswhzl.api.entity.Permission;
import com.jswhzl.api.entity.Role;
import com.jswhzl.api.mapper.PermissionMapper;
import com.jswhzl.api.mapper.RoleMapper;
import com.jswhzl.api.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jswhzl.common.vo.MenuVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    private static final long serialVersionUID = -5675595787280449636L;

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<MenuVO> findMenuByUserId(Long userId) {
        Role role = roleMapper.findByUserId(userId);
        if (null == role){
            throw new RuntimeException("用户角色信息不存在");
        }else{
            return getMenuVOList(role.getRoleId(),null, null, null);
        }
    }

    @Override
    public List<Permission> findListByUserId(Long userId) {
        Role role = roleMapper.findByUserId(userId);
        if (null == role){
            throw new RuntimeException("用户角色信息不存在");
        }else{
            return this.permissionMapper.findMenu(role.getRoleId(), 0L, null, null);
        }
    }

    @Override
    public List<Permission> findListByRoleId(Long roleId) {
        return this.permissionMapper.findMenu(roleId, 0L, null, null);
    }

    private List<MenuVO> getMenuVOList(Long roleId, Long parentId, Integer level, Integer type){
        List<MenuVO> result = new ArrayList<>();
        List<Permission> menusList = permissionMapper.findMenu(roleId,parentId, level, type);
        if (null != menusList && menusList.size() > 0){
            menusList.forEach(menu->{
                MenuVO menuVO = new MenuVO();
                menuVO.setTitle(menu.getPerName());
                menuVO.setName(menu.getPerCode());
                menuVO.setIcon(menu.getIcon());
                menuVO.setJump(menu.getUrl());
                menuVO.setSpread(false);
                menuVO.setList(getMenuVOList(null, menu.getPerId(), null, null));
                result.add(menuVO);
            });
        }
        return result;
    }
}
