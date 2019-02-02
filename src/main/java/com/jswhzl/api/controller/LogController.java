/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jswhzl.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jswhzl.api.entity.Log;
import com.jswhzl.api.service.impl.LogServiceImpl;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.util.Pager;


/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
@RestController
@RequestMapping("/api/log")
public class LogController {
	
	private final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	
	@Autowired
    private LogServiceImpl logService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param dictionary
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<Log> pager, Log log,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Log> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(log.getUsername()), "username", log.getUsername());
            wrapper.eq(StringUtils.isNotBlank(log.getOperation()), "operation", log.getOperation());
            wrapper.eq(StringUtils.isNotBlank(log.getMethod()), "method", log.getMethod());
            List records = logService.page(pager, wrapper).getRecords();
            int count = logService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }
	
    /**
     * 查询所有列表
     * @param dictionary
     * @return
     */
    @GetMapping
    public ReturnEntity selectList(Log log,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
        	QueryWrapper<Log> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(log.getUsername()), "username", log.getUsername());
            wrapper.eq(StringUtils.isNotBlank(log.getOperation()), "operation", log.getOperation());
            wrapper.eq(StringUtils.isNotBlank(log.getMethod()), "method", log.getMethod());
            List<Log> list = logService.list(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }
    
}
