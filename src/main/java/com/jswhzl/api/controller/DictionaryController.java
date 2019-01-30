package com.jswhzl.api.controller;


import com.jswhzl.api.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import com.jswhzl.common.annotation.SysLog;
import com.jswhzl.common.base.BaseController;
import org.springframework.validation.FieldError;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.util.Pager;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.jswhzl.api.service.impl.DictionaryServiceImpl;
import com.jswhzl.api.entity.Dictionary;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController extends BaseController {
    private static final long serialVersionUID = 7492461415672362601L;
    private final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
    @Autowired
    private DictionaryServiceImpl dictionaryService;
    @Autowired
    private TokenService tokenService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param dictionary
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<Dictionary> pager, Dictionary dictionary,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicName()), "dicName", dictionary.getDicName());
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicValue()), "dicValue", dictionary.getDicValue());
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicType()), "dicType", dictionary.getDicType());
            List records = dictionaryService.page(pager, wrapper).getRecords();
            int count = dictionaryService.count(wrapper);
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
    public ReturnEntity selectList(Dictionary dictionary,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicName()), "dicName", dictionary.getDicName());
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicValue()), "dicValue", dictionary.getDicValue());
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicType()), "dicType", dictionary.getDicType());
            List<Dictionary> list = dictionaryService.list(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
        try {
            Dictionary dictionary = dictionaryService.getById(id);
            if (null != dictionary) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, dictionary);
            } else {
                return new ReturnEntitySuccess(Constants.MSG_FIND_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

    /**
     * 新增
     *
     * @param dictionary
     * @param result
     * @return
     */
    @SysLog("新增字典")
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody Dictionary dictionary, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, dictionary);
        } else {
            QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicValue()), "dicValue", dictionary.getDicValue());
            wrapper.eq(StringUtils.isNotBlank(dictionary.getDicType()), "dicType", dictionary.getDicType());
            if (dictionaryService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, dictionary);
            } else {
                dictionary.setCreateId(tokenService.getUserId(request, response));
                dictionary.setCreateTime(LocalDateTime.now());
                if (dictionaryService.save(dictionary)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, dictionary);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, dictionary);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param dictionary
     * @param result
     * @return
     */
    @SysLog("修改字典")
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Dictionary dictionary, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), dictionary);
        } else {
            if (null == dictionaryService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, dictionary);
            } else {
                dictionary.setDicId(id);
                dictionary.setUpdateId(tokenService.getUserId(request, response));
                dictionary.setUpdateTime(LocalDateTime.now());
                if (dictionaryService.updateById(dictionary)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, dictionary);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, dictionary);
                }

            }
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @SysLog("删除字典")
    @DeleteMapping("/{id}")
    public ReturnEntity deleteById(@PathVariable("id") Long id) {
        if (null == dictionaryService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (dictionaryService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

}
