package com.jswhzl.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jswhzl.api.entity.Permission;
import com.jswhzl.api.entity.User;
import com.jswhzl.api.service.TokenService;
import com.jswhzl.api.service.impl.PermissionServiceImpl;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author xuchao
 * @ClassName PermissionInterceptor
 * @Description 权限菜单拦截器
 * @date 2018-10-19 8:25
 **/
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private PermissionServiceImpl permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("被拦截啦！！！！！！！！！！");
        String uri = request.getRequestURI();
        System.out.println("uri=" + uri);
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization=" + authorization);
        String method = request.getMethod();
        System.out.println("method=" + method);
        boolean authFlag = false;

        if (StringUtils.isNotBlank(authorization)) {
            Token tokenInfo = tokenService.getTokenInfo(request, response);
            if (null != tokenInfo) {
                if (tokenService.getExpire(request, response)<300){
                    tokenService.updateExpire(request, response);
                }
                User user = tokenService.getUserInfo(request, response);
                if (null != user) {
                    String url = method + ":" + uri.replace("/hahgz", "");
                    System.out.println("url=" + url);
                    /*List<Permission> permissions = permissionService.findListByUserId(user.getUserId());
                    for (Permission permission : permissions) {
                        if (StringUtils.isNotBlank(permission.getPerCode())) {
                            if (url.contains(permission.getPerCode())) {
                                authFlag = true;
                                break;
                            }
                        }
                    }*/
                    authFlag = true;
                    if (!authFlag) {
                        PrintWriter writer = response.getWriter();
                        writer.append(JSONObject.toJSONString(new ReturnEntityError("无权访问，请联系管理员")));
                    }
                } else {
                    PrintWriter writer = response.getWriter();
                    writer.append(JSONObject.toJSONString(new ReturnEntityError(1001, "用户不存在")));
                }
            } else {
                PrintWriter writer = response.getWriter();
                writer.append(JSONObject.toJSONString(new ReturnEntityError(1001, "token已过期")));
            }
        } else {
            PrintWriter writer = response.getWriter();
            writer.append(JSONObject.toJSONString(new ReturnEntityError(1001, "未获取到token")));
            authFlag = false;
        }
        return authFlag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(tokenService.getExpire(request, response));
        String uri = request.getRequestURI();
        System.out.println("after uri=" + uri);
        String authorization = request.getHeader("Authorization");
        System.out.println("after authorization=" + authorization);
        String method = request.getMethod();
        System.out.println("after method=" + method);
        /*Map map=request.getParameterMap();
        Set keSet=map.entrySet();
        for(Iterator itr = keSet.iterator(); itr.hasNext();){
            Map.Entry me=(Map.Entry)itr.next();
            Object ok=me.getKey();
            Object ov=me.getValue();
            String[] value=new String[1];
            if(ov instanceof String[]){
                value=(String[])ov;
            }else{
                value[0]=ov.toString();
            }

            for(int k=0;k<value.length;k++){
                System.out.println(ok+"="+value[k]);
            }
        }*/
    }
}
