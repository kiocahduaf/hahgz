package com.jswhzl.common.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.jswhzl.api.entity.User;
import com.jswhzl.common.base.Base58Util;

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
