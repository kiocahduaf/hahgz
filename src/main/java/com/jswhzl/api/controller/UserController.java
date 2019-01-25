package com.jswhzl.api.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jswhzl.api.entity.User;
import com.jswhzl.api.service.impl.RoleServiceImpl;
import com.jswhzl.api.service.impl.UserServiceImpl;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.dto.UserDTO;
import com.jswhzl.common.util.Md5Util;
import com.jswhzl.common.util.Pager;
import com.jswhzl.common.vo.UserVO;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private static final long serialVersionUID = 8439640366444489946L;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param user
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<User> pager, User user,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(user.getUserName()), "userName", user.getUserName());
            wrapper.eq(StringUtils.isNotBlank(user.getWxId()), "wxId", user.getWxId());
            wrapper.like(StringUtils.isNotBlank(user.getRealName()), "realName", user.getRealName());
            wrapper.eq(StringUtils.isNotBlank(user.getIdentityCard()), "identityCard", user.getIdentityCard());
            wrapper.eq(StringUtils.isNotBlank(user.getTel()), "tel", user.getTel());
            List<User> records = userService.page(pager, wrapper).getRecords();
            List<UserVO> userVOList = new ArrayList<>();
            records.forEach(record -> {
                UserVO uv = new UserVO();
                uv.setUser(record);
                uv.setRole(roleService.findByUserId(record.getUserId()));
                userVOList.add(uv);
            });
            int count = userService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, userVOList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     *
     * @param user
     * @return
     */
    @GetMapping
    public ReturnEntity selectList(User user,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(user.getUserName()), "userName", user.getUserName());
            wrapper.eq(StringUtils.isNotBlank(user.getWxId()), "wxId", user.getWxId());
            wrapper.like(StringUtils.isNotBlank(user.getRealName()), "realName", user.getRealName());
            wrapper.eq(StringUtils.isNotBlank(user.getIdentityCard()), "identityCard", user.getIdentityCard());
            wrapper.eq(StringUtils.isNotBlank(user.getTel()), "tel", user.getTel());
            wrapper.eq(user.getUserType() != null, "userType", user.getUserType());
            List<User> list = userService.list(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
        try {
            UserVO uv = userService.findDetailByUserId(id);
            if (null != uv) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, uv);
            } else {
                return new ReturnEntitySuccess(Constants.MSG_FIND_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

    /**
     * 新增
     *
     * @param user
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody UserDTO user, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if (Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)) {
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, user);
        } else {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(user.getWxId()), "wxId", user.getWxId());
            wrapper.eq(StringUtils.isNotBlank(user.getUserName()), "userName", user.getUserName());
            if (userService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, user);
            } else {
                if (StringUtils.isNotBlank(user.getUserPwd())) {
                    user.setUserPwd(Md5Util.getSaltMD5(user.getUserPwd()));
                } else {
                    user.setUserPwd(Md5Util.getSaltMD5("123456"));
                }
                if (null != user.getRoleId()) {
                    if (Constants.ROLE_EXPERT.equals(String.valueOf(user.getRoleId()))) {
                        //专家组
                        user.setUserType(4);
                    } else if (Constants.ROLE_APPRAISALORGANIZATION.equals(String.valueOf(user.getRoleId()))) {
                        //鉴定中心
                        user.setUserType(2);
                    } else if (Constants.ROLE_CHECKORGANIZATION.equals(String.valueOf(user.getRoleId()))) {
                        //检测机构
                        user.setUserType(3);
                    } else if (Constants.ROLE_WORK_USER.equals(String.valueOf(user.getRoleId()))) {
                        //检测机构施工人员
                        user.setUserType(5);
                    } else if (Constants.ROLE_SUPER_ADMIN.equals(String.valueOf(user.getRoleId()))) {
                        //超级管理员
                        user.setUserType(0);
                    } else {
                        user.setUserType(1);
                    }
                } else {
                    user.setUserType(1);
                }
                user.setCreateTime(LocalDateTime.now());
                try {
                    if (userService.addUser(user)) {
                        return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, user);
                    } else {
                        return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, user);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param user
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody UserDTO user, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), user);
        } else {
            if (null == userService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, user);
            } else {
                user.setUserId(id);
                if (null != user.getRoleId()) {
                    if (Constants.ROLE_EXPERT.equals(String.valueOf(user.getRoleId()))) {
                        //专家组
                        user.setUserType(4);
                    } else if (Constants.ROLE_APPRAISALORGANIZATION.equals(String.valueOf(user.getRoleId()))) {
                        //鉴定中心
                        user.setUserType(2);
                    } else if (Constants.ROLE_CHECKORGANIZATION.equals(String.valueOf(user.getRoleId()))) {
                        //检测机构
                        user.setUserType(3);
                    } else if (Constants.ROLE_WORK_USER.equals(String.valueOf(user.getRoleId()))) {
                        //检测机构施工人员
                        user.setUserType(5);
                    } else if (Constants.ROLE_SUPER_ADMIN.equals(String.valueOf(user.getRoleId()))) {
                        //超级管理员
                        user.setUserType(0);
                    } else {
                        user.setUserType(1);
                    }
                } else {
                    user.setUserType(1);
                }
                user.setUpdateTime(LocalDateTime.now());
                try {
                    if (userService.updateUser(user)) {
                        return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, user);
                    } else {
                        return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, user);
                }
            }
        }
    }

    /**
     * 删除
     *
     * @param id 用户ID
     * @return ReturnEntity
     */
    @DeleteMapping("/{id}")
    public ReturnEntity deleteById(@PathVariable("id") Long id) {
        if (null == userService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        } else {
            if (userService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            } else {
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

    /**
     * 根据depId查询用户列表
     *
     * @param depId 部门ID
     * @return ReturnEntity
     */
    @GetMapping("/findUserListByDepId/{depId}")
    public ReturnEntity findUserListByDepId(@PathVariable("depId") Long depId) {
        try {
            List<User> users = userService.findUserListByDepId(depId);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

    /**
     * 获取三个不相同随机数字
     *
     * @param count
     * @param randomSet
     */
    private void getRandomNum(Integer count, Set<Integer> randomSet) {
        Random random = new Random();
        int one = random.nextInt(count);
        randomSet.add(one);
        if (randomSet.size() < 3) {
            getRandomNum(count, randomSet);
        }
    }

}
