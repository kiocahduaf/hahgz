/**
 * Copyright (C), 2015-2018, 江苏物合智联科技有限公司
 * FileName: ExceptionHandle
 * Author:   xuchao
 * Date:     2018/11/22 0022 16:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jswhzl.common.exception;

import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈一句话功能简述〉<br> 
 * Description: 
 *
 * @author xuchao
 * @create 2018/11/22 0022 16:46
 * @since 1.0.0
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ReturnEntity handle(Exception e){
        if (e instanceof RuntimeException) {
            return new ReturnEntityError(e.getMessage());
        }else {
            return new ReturnEntityError(-1, "未知错误");
        }
    }
}
