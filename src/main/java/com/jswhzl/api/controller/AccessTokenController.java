package com.jswhzl.api.controller;


import org.springframework.web.bind.annotation.*;
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
import com.jswhzl.api.service.impl.AccessTokenServiceImpl;
import com.jswhzl.api.entity.AccessToken;
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
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/api/accessToken")
public class AccessTokenController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(AccessTokenController.class);
    @Autowired
    private AccessTokenServiceImpl accessTokenService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param accessToken
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<AccessToken> pager, AccessToken accessToken,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<AccessToken> wrapper = new QueryWrapper<>();
            List<AccessToken> records = accessTokenService.page(pager, wrapper).getRecords();
            int count = accessTokenService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param accessToken
     * @return
     */
    @GetMapping
    public ReturnEntity selectList(AccessToken accessToken,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<AccessToken> wrapper = new QueryWrapper<>();
            List<AccessToken> list = accessTokenService.list(wrapper);
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
            AccessToken accessToken = accessTokenService.getById(id);
            if (null != accessToken) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, accessToken);
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
     * @param accessToken
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody AccessToken accessToken, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, accessToken);
        } else {
            QueryWrapper<AccessToken> wrapper = new QueryWrapper<>();
            if (accessTokenService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, accessToken);
            } else {
                if (accessTokenService.save(accessToken)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, accessToken);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, accessToken);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param accessToken
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody AccessToken accessToken, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), accessToken);
        } else {
            if (null == accessTokenService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, accessToken);
            } else {
                if (accessTokenService.updateById(accessToken)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, accessToken);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, accessToken);
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
    @DeleteMapping("/{id}")
    public ReturnEntity deleteById(@PathVariable("id") Long id) {
        if (null == accessTokenService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (accessTokenService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

}
