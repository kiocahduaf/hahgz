package com.jswhzl.common.util;

import com.jswhzl.api.entity.User;
import com.jswhzl.api.service.impl.UserServiceImpl;
import com.jswhzl.common.base.Base58Util;
import com.jswhzl.common.bean.Token;
import com.jswhzl.common.cache.TokenRedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author xuchao
 * @ClassName TokenUtil
 * @Description token manage
 * @date 2018-10-29 9:43
 **/
@Component
public class TokenUtil implements Serializable {
    private static final long serialVersionUID = 1896646223429064513L;


    public static String getToken(User user, HttpServletRequest request, HttpServletResponse response) {
        return Base58Util.getIDCode();
    }


}
