package com.jswhzl.api.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jswhzl.api.entity.UserRole;
import com.jswhzl.api.service.impl.UserRoleServiceImpl;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.util.Pager;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@RestController
@RequestMapping("/api/userRole")
public class UserRoleController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(UserRoleController.class);
    @Autowired
    private UserRoleServiceImpl userRoleService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param userRole
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<UserRole> pager, UserRole userRole,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            List records = userRoleService.page(pager, wrapper).getRecords();
            int count = userRoleService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param userRole
     * @return
     */
    @GetMapping("/list")
    public ReturnEntity selectList(UserRole userRole,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            List<UserRole> list = userRoleService.list(wrapper);
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
            UserRole userRole = userRoleService.getById(id);
            if (null != userRole) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, userRole);
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
     * @param userRole
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody UserRole userRole, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, userRole);
        } else {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            if (userRoleService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, userRole);
            } else {
                if (userRoleService.save(userRole)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, userRole);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, userRole);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param userRole
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody UserRole userRole, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), userRole);
        } else {
            if (null == userRoleService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, userRole);
            } else {
                if (userRoleService.updateById(userRole)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, userRole);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, userRole);
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
        if (null == userRoleService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (userRoleService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

}
