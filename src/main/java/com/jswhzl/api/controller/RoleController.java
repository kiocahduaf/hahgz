package com.jswhzl.api.controller;


import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.jswhzl.api.entity.Role;
import com.jswhzl.api.service.TokenService;
import com.jswhzl.api.service.impl.RoleServiceImpl;
import com.jswhzl.common.annotation.SysLog;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.dto.PermissionDTO;
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
@RequestMapping("/api/role")
public class RoleController extends BaseController {
    private static final long serialVersionUID = 6619509625272960840L;
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private TokenService tokenService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param role
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<Role> pager, Role role,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(role.getRoleName()),"roleName", role.getRoleName());
            List<Role> records = roleService.page(pager, wrapper).getRecords();
            int count = roleService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param role
     * @return
     */
    @GetMapping
    public ReturnEntity selectList(Role role,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(role.getRoleName()),"roleName", role.getRoleName());
            List<Role> list = roleService.list(wrapper);
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
    @SysLog("查看详情")
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
        try {
            Role role = roleService.getById(id);
            if (null != role) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, role);
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
     * @param role
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody Role role, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, role);
        } else {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(role.getRoleName()),"roleName", role.getRoleName());
            if (roleService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, role);
            } else {
                role.setCreateId(tokenService.getUserId(request, response));
                role.setCreateTime(LocalDateTime.now());
                if (roleService.save(role)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, role);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, role);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param role
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Role role, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), role);
        } else {
            if (null == roleService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, role);
            } else {
                role.setRoleId(id);
                role.setUpdateId(tokenService.getUserId(request, response));
                role.setUpdateTime(LocalDateTime.now());
                if (roleService.updateById(role)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, role);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, role);
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
        if (null == roleService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (roleService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

    /**
     * 授权
     *
     * @param id
     * @param perIds
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/grant/{id}")
    public ReturnEntity grant(@PathVariable("id") Long id, @RequestBody PermissionDTO permissionDTO,
                              HttpServletRequest request, HttpServletResponse response) {
        try {
            if (roleService.grant(id, permissionDTO.getPerIds())) {
                return new ReturnEntitySuccess("授权成功");
            } else {
                return new ReturnEntityError("授权失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError("授权失败");
        }
    }
}
