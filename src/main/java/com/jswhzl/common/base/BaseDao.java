/**
 *
 */
package com.jswhzl.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * BaseDao接口基类
 *
 * @param <T>
 * @author xuchao
 */
public interface BaseDao<T> extends BaseMapper<T>,Serializable {


}
