package com.jswhzl.common.bean;

import com.jswhzl.common.config.Constants;
import lombok.Data;

/**
 * @author xuchao
 * @ClassName ReturnEntityError
 * @Description 出错时返回
 * @date 2018-10-17 11:40
 **/
@Data
public class ReturnEntityError extends ReturnEntity {

    private static final long serialVersionUID = -1821372766651550968L;

    public ReturnEntityError() {
    }

    public ReturnEntityError(Integer code, String msg, Integer count, Object data) {
        super(code, msg, count, data);
    }

    public ReturnEntityError(String msg) {
        super(Constants.CODE_ERROR, msg, null, null);
    }

    public ReturnEntityError(Integer code, String msg) {
        super(code, msg, null, null);
    }

    public ReturnEntityError(String msg, Object data) {
        super(Constants.CODE_ERROR, msg, null, data);
    }

    public ReturnEntityError(Integer code, String msg, Object data) {
        super(code, msg, null, data);
    }

    public ReturnEntityError(String msg, Integer count, Object data) {
        super(Constants.CODE_ERROR, msg, count, data);
    }
    
    
}
