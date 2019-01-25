package com.jswhzl.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jswhzl.api.entity.User;
import com.jswhzl.api.service.impl.UserServiceImpl;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.Token;
import com.jswhzl.common.cache.TokenRedisCache;

/**
 * @author xuchao
 * @ClassName TokenController
 * @Description
 * @date 2018-11-08 17:49
 **/
@Service
public class TokenService extends BaseController {
    private static final long serialVersionUID = 935258820629733612L;
    @Autowired
    private TokenRedisCache tokenRedisCache;
    @Autowired
    private UserServiceImpl userService;

    public User getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getUserId(request, response);
        return userService.getById(userId);
    }

    public Long getUserId(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (tokenRedisCache.exists(tokenCode)) {
            Token token = tokenRedisCache.selectById(tokenCode);

            if (null != token) {
                return token.getUserId();
            } else {
                throw new RuntimeException("user is null");
            }
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    public Token getTokenInfo(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (tokenRedisCache.exists(tokenCode)) {
            return tokenRedisCache.selectById(tokenCode);
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    public Long getExpire(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (tokenRedisCache.exists(tokenCode)) {
            return tokenRedisCache.getExpire(tokenCode);
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    public boolean updateExpire(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (tokenRedisCache.exists(tokenCode)) {
            return tokenRedisCache.setExpire(tokenCode, 30 * 60L);
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        return tokenRedisCache.deleteById(tokenCode);
    }
}
