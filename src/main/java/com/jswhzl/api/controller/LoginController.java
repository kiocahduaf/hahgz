package com.jswhzl.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jswhzl.api.entity.Role;
import com.jswhzl.api.entity.User;
import com.jswhzl.api.service.impl.RoleServiceImpl;
import com.jswhzl.api.service.impl.UserServiceImpl;
import com.jswhzl.common.annotation.SysLog;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.bean.Token;
import com.jswhzl.common.cache.TokenRedisCache;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.dto.UserDTO;
import com.jswhzl.common.util.Md5Util;
import com.jswhzl.common.util.TokenUtil;
import com.jswhzl.common.vo.UserVO;

/**
 * @author xuchao
 * @ClassName LoginController
 * @Description 登录与退出
 * @date 2018-10-29 14:10
 **/
@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {
    private static final long serialVersionUID = 7058712925721353762L;
    private static Logger logger = LoggerFactory.getLogger(LoginController.class.getName());

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TokenRedisCache tokenRedisCache;
    @Autowired
    private RoleServiceImpl roleService;

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param request
     * @param response
     * @return
     */
    @SysLog("用户登录")
    @PostMapping("/login")
    public ReturnEntity login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                              HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("userName", userName);
            User user = userService.getOne(wrapper);
            if (null != user) {
                if (Md5Util.getSaltverifyMD5(password, user.getUserPwd())) {
                    String authorization = TokenUtil.getToken(user, request, response);
                    Token token = new Token();
                    token.setExpireSecond(System.currentTimeMillis());
                    token.setUserId(user.getUserId());
                    tokenRedisCache.insert(authorization, token);
                    return new ReturnEntitySuccess(Constants.CODE_SUCCESS, Constants.MSG_LOGIN_SUCCESS, null, authorization);
                } else {
                    return new ReturnEntityError(Constants.CODE_LOGIN_PASSWORD_ERROR, Constants.MSG_LOGIN_PASSWORD_ERROR);
                }
            } else {
                return new ReturnEntityError(Constants.CODE_LOGIN_USERNAME_ERROR, Constants.MSG_LOGIN_USERNAME_ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ReturnEntityError(Constants.CODE_LOGIN_ERROR, Constants.MSG_LOGIN_ERROR);
        }
    }

    /**
     * 获取登录信息
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/getLoginInfo")
    public ReturnEntity getLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorization)) {
            if (tokenRedisCache.exists(authorization)) {
                Token token = tokenRedisCache.selectById(authorization);
                User user = userService.getById(token.getUserId());
                if (null != user) {
                    Role role = roleService.findByUserId(user.getUserId());
                    UserVO userVO = new UserVO();
                    userVO.setUser(user);
                    userVO.setRole(role);
                    return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, userVO);
                } else {
                    return new ReturnEntityError(1001, Constants.MSG_FIND_NOT_FOUND);
                }
            } else {
                return new ReturnEntityError(1001, "token已过期");
            }
        } else {
            return new ReturnEntityError(1001, Constants.MSG_TOKEN_NOT_FOUND);
        }
    }

    /**
     * 微信登录
     *
     * @param openId
     * @param tel
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/wxLogin/{openId}/{tel}")
    public ReturnEntity wxLogin(@PathVariable("openId") String openId,
                                @PathVariable("tel") String tel,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDTO us = new UserDTO();
            us.setWxId(openId);
            us.setTel(tel);
            User user = userService.wxLogin(us);
            if (null != user) {
                String authorization = TokenUtil.getToken(user, request, response);
                Token token = new Token();
                token.setExpireSecond(30 * 60L);
                token.setUserId(user.getUserId());
                tokenRedisCache.insert(authorization, token, token.getExpireSecond());
                return new ReturnEntitySuccess(Constants.CODE_SUCCESS, Constants.MSG_LOGIN_SUCCESS, null, authorization);
            } else {
                return new ReturnEntityError(Constants.CODE_LOGIN_USERNAME_ERROR, "用户不存在");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ReturnEntityError(Constants.CODE_LOGIN_ERROR, Constants.MSG_LOGIN_ERROR);
        }
    }

    /**
     * 根据wxId获取用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/getUserInfoByWxId/{wxId}")
    public ReturnEntity getUserInfoByWxId(@PathVariable("wxId") String wxId,
                                          HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(wxId), "wxId", wxId);
        User user = userService.getOne(wrapper);
        if (null != user) {
            Role role = roleService.findByUserId(user.getUserId());
            UserVO userVO = new UserVO();
            userVO.setUser(user);
            userVO.setRole(role);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, userVO);
        } else {
            return new ReturnEntityError(1001, Constants.MSG_FIND_NOT_FOUND);
        }
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @return
     */
    @SysLog("用户退出")
    @GetMapping("/logout")
    public ReturnEntity logout(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorization)) {
            tokenRedisCache.deleteById(authorization);
            return new ReturnEntitySuccess(1001, Constants.MSG_LOGOUT_SUCCESS);
        } else {
            return new ReturnEntityError(Constants.MSG_LOGOUT_ERROR);
        }
    }
}
