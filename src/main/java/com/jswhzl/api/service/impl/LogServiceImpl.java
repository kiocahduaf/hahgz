package com.jswhzl.api.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jswhzl.api.entity.Log;
import com.jswhzl.api.mapper.LogMapper;
import com.jswhzl.api.service.ILogService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
