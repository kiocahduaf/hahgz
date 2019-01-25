package com.jswhzl.common.bean;

import com.jswhzl.common.config.Constants;
import lombok.Data;

/**
 * @author xuchao
 * @ClassName ReturnEntitySuccess
 * @Description 正确时返回
 * @date 2018-10-17 11:40
 **/
@Data
public class ReturnEntitySuccess extends ReturnEntity {

    private static final long serialVersionUID = 8063914801766963383L;

    public ReturnEntitySuccess() {
    }

    public ReturnEntitySuccess(Integer code, String msg, Integer count, Object data) {
        super(code, msg, count, data);
    }

    public ReturnEntitySuccess(String msg) {
        super(Constants.CODE_SUCCESS, msg, null, null);
    }

    public ReturnEntitySuccess(Integer code, String msg) {
        super(code, msg, null, null);
    }

    public ReturnEntitySuccess(String msg, Object data) {
        super(Constants.CODE_SUCCESS, msg, null, data);
    }

    public ReturnEntitySuccess(String msg, Integer count, Object data) {
        super(Constants.CODE_SUCCESS, msg, count, data);
    }
}
