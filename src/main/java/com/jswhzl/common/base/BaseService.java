package com.jswhzl.common.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * @author xuchao
 * @ClassName BaseService
 * @Description 基础Service
 * @date 2018-09-03 19:17
 **/
public interface BaseService<T> extends IService<T>,Serializable {
}
