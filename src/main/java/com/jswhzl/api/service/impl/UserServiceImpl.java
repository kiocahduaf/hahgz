package com.jswhzl.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jswhzl.api.entity.User;
import com.jswhzl.api.entity.UserRole;
import com.jswhzl.api.mapper.UserMapper;
import com.jswhzl.api.mapper.UserRoleMapper;
import com.jswhzl.api.service.IUserService;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.dto.UserDTO;
import com.jswhzl.common.util.Md5Util;
import com.jswhzl.common.vo.UserVO;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final long serialVersionUID = 1656560942199218516L;

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleServiceImpl roleService;

    @Override
    public UserVO findDetailByUserId(Long userId) {
        User user = getById(userId);
        if (null != user) {
            UserVO uv = new UserVO();
            uv.setUser(user);
            uv.setRole(roleService.findByUserId(userId));
            return uv;
        }else{
            throw new RuntimeException(Constants.MSG_FIND_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public boolean addUser(UserDTO user) {
        if (userMapper.insert(user) > 0) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(user.getRoleId());
            if (userRole.insert()) {
            	return true;
            } else {
                throw new RuntimeException("角色关联失败");
            }
        } else {
            throw new RuntimeException("用户添加失败");
        }
    }

    @Override
    @Transactional
    public boolean updateUser(UserDTO user) {
        if (userMapper.updateById(user) > 0) {
            //如果修改成功，就去删除旧的用户角色关联关系
            if (null != user.getRoleId()) {
                QueryWrapper<UserRole> wrapperUserRole = new QueryWrapper<>();
                wrapperUserRole.eq("userId", user.getUserId());
                if (userRoleMapper.delete(wrapperUserRole) >= 0) {
                    //重新添加新的用户角色关联关系
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getUserId());
                    userRole.setRoleId(user.getRoleId());
                    if (!userRole.insert()) {
                        throw new RuntimeException("角色关联失败");
                    }
                }
            }
            return true;
        } else {
            throw new RuntimeException("用户修改失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findUserListByDepId(Long depId) {
        return userMapper.findUserListByDepId(depId);
    }

    @Override
    @Transactional
    public User wxLogin(UserDTO userDTO) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("wxId", userDTO.getWxId());
        User user = userMapper.selectOne(wrapper);
        //如果根据openId能查到用户，直接返回，否则根据联系电话查询匹配
        if (null == user) {
            QueryWrapper<User> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("tel", userDTO.getTel());
            User userByTel = userMapper.selectOne(wrapper2);
            //如果电话号码存在，则绑定该openId，否则就新增一个用户
            if (null != userByTel) {
                userByTel.setWxId(userDTO.getWxId());
                if (userMapper.updateById(userByTel) >= 0) {
                    return userByTel;
                } else {
                    throw new RuntimeException("用户信息绑定失败");
                }
            } else {
                userDTO.setRoleId(Constants.ROLE_NORMAL);
                userDTO.setUserPwd(Md5Util.getSaltMD5("123456"));
                if (userMapper.insert(userDTO) > 0) {
                    return userDTO;
                } else {
                    throw new RuntimeException("用户信息查询失败");
                }
            }
        } else {
            return user;
        }
    }
}
