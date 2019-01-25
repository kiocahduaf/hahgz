package com.jswhzl.common.config;

/**
 * @author xuchao
 * @ClassName Constants
 * @Description
 * @date 2018-10-17 9:28
 **/
public class Constants {

    /**
     * Content-Type：application/json; charset=utf-8
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    /**
     * 默认日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMATTER  = "yyyy-MM-dd HH:mm:ss";
    /**
     * AES加密解密的password
     */
    public static final String AES_PASSWORD = "xuchao2018";
    /**
     * 默认系统登录有效期
     */
    public static final long DEFAULT_EXPIRE_SECOND = 18000;

    public static final Integer CODE_SUCCESS = 0;
    public static final Integer CODE_ERROR = 1;

    public static final String MSG_ERROR_MSG = "数据异常";
    public static final String MSG_ERROR_CANNOT_NULL = "不能为null";
    public static final String MSG_CONNECTION_ERROR = "连接异常";
    public static final String MSG_FIND_NOT_FOUND = "未查询到结果";
    public static final String MSG_FIND_EXISTED = "数据已存在";
    public static final String MSG_FIND_SUCCESS = "查询成功";
    public static final String MSG_FIND_FAILED = "查询失败";
    public static final String MSG_INSERT_SUCCESS = "新增成功";
    public static final String MSG_INSERT_FAILED = "新增失败";
    public static final String MSG_UPDATE_SUCCESS = "修改成功";
    public static final String MSG_UPDATE_FAILED = "修改失败";
    public static final String MSG_DELETE_SUCCESS = "删除成功";
    public static final String MSG_DELETE_FAILED = "删除失败";
    public static final String MSG_UPLOAD_SUCCESS = "上传成功";
    public static final String MSG_UPLOAD_FAILED = "上传失败";


    public static final String MSG_TOKEN_NOT_FOUND = "未获取到token";
    public static final String MSG_LOGIN_SUCCESS = "登录成功";
    public static final String MSG_LOGOUT_SUCCESS = "登录成功";
    public static final Integer CODE_LOGIN_ERROR = 1201;
    public static final String MSG_LOGIN_ERROR = "登录失败";
    public static final String MSG_LOGOUT_ERROR = "登录失败";
    public static final Integer CODE_LOGIN_USERNAME_ERROR = 1202;
    public static final String MSG_LOGIN_USERNAME_ERROR = "用户名错误";
    public static final Integer CODE_LOGIN_PASSWORD_ERROR = 1203;
    public static final String MSG_LOGIN_PASSWORD_ERROR = "密码错误";

    public static final Long ROLE_NORMAL = 1065434584646107138L;
    public static final String ROLE_SUPER_ADMIN = "1063349544663580673";
    public static final String ROLE_EXPERT = "1076029311690989570";
    public static final String ROLE_APPRIASAL = "1073504066425815042";
    public static final String ROLE_CHECKORGANIZATION = "1076030550642253826";
    public static final String ROLE_APPRAISALORGANIZATION = "1073504066425815042";
    public static final String ROLE_WORK_USER = "1082568472149229570";

    /**
     * 鉴定受理通知模板ID
     */
    public static final String ACCEPT_TRMPLATE_ID = "dF9Wzamyl0XH7ooQoByaGnqJo2tIqck9ZiexkJZuLds";

    /**
     * 危房通知书通知模板ID
     */
    public static final String DANGER_TRMPLATE_ID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

}
