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
import com.jswhzl.api.service.impl.RolePermissionServiceImpl;
import com.jswhzl.api.entity.RolePermission;
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
@RequestMapping("/api/rolePermission")
public class RolePermissionController extends BaseController {
    private static final long serialVersionUID = -7721834056923881420L;
    private final Logger logger = LoggerFactory.getLogger(RolePermissionController.class);
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param rolePermission
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<RolePermission> pager, RolePermission rolePermission,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
            wrapper.eq(null != rolePermission.getRoleId(), "roleId", rolePermission.getRoleId());
            wrapper.eq(null != rolePermission.getPerId(), "perId", rolePermission.getPerId());
            List records = rolePermissionService.page(pager, wrapper).getRecords();
            int count = rolePermissionService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param rolePermission
     * @return
     */
    @GetMapping("/list")
    public ReturnEntity selectList(RolePermission rolePermission,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
            wrapper.eq(null != rolePermission.getRoleId(), "roleId", rolePermission.getRoleId());
            wrapper.eq(null != rolePermission.getPerId(), "perId", rolePermission.getPerId());
            List<RolePermission> list = rolePermissionService.list(wrapper);
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
            RolePermission rolePermission = rolePermissionService.getById(id);
            if (null != rolePermission) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, rolePermission);
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
     * @param rolePermission
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody RolePermission rolePermission, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, rolePermission);
        } else {
            QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
            wrapper.eq(null != rolePermission.getRoleId(), "roleId", rolePermission.getRoleId());
            wrapper.eq(null != rolePermission.getPerId(), "perId", rolePermission.getPerId());
            if (rolePermissionService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, rolePermission);
            } else {
                if (rolePermissionService.save(rolePermission)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, rolePermission);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, rolePermission);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param rolePermission
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody RolePermission rolePermission, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), rolePermission);
        } else {
            if (null == rolePermissionService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, rolePermission);
            } else {
                rolePermission.setId(id);
                if (rolePermissionService.updateById(rolePermission)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, rolePermission);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, rolePermission);
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
        if (null == rolePermissionService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (rolePermissionService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

}
