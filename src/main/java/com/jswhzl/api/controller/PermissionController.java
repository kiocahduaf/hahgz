package com.jswhzl.api.controller;


import com.jswhzl.api.service.TokenService;
import com.jswhzl.common.vo.MenuVO;
import org.apache.commons.lang3.StringUtils;
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
import com.jswhzl.api.service.impl.PermissionServiceImpl;
import com.jswhzl.api.entity.Permission;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <p>
 *  前端控制器--权限菜单
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    @Autowired
    private PermissionServiceImpl permissionService;
    @Autowired
    TokenService tokenService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param permission
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<Permission> pager, Permission permission,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Permission> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(permission.getPerName()), "perName", permission.getPerName());
            wrapper.eq(StringUtils.isNotBlank(permission.getPerCode()), "perCode", permission.getPerCode());
            wrapper.eq(null != permission.getParentId(), "parentId", permission.getParentId());
            List records = permissionService.page(pager, wrapper).getRecords();
            int count = permissionService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param permission
     * @return
     */
    @GetMapping("/list")
    public ReturnEntity selectList(Permission permission,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Permission> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(permission.getPerName()), "perName", permission.getPerName());
            wrapper.eq(StringUtils.isNotBlank(permission.getPerCode()), "perCode", permission.getPerCode());
            wrapper.eq(null != permission.getParentId(), "parentId", permission.getParentId());
            wrapper.orderByAsc("type", "level", "seq");
            List<Permission> list = permissionService.list(wrapper);
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
            Permission permission = permissionService.getById(id);
            if (null != permission) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, permission);
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
     * @param permission
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody Permission permission, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, permission);
        } else {
            QueryWrapper<Permission> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(permission.getPerCode()), "perCode", permission.getPerCode());
            if (permissionService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, permission);
            } else {
                permission.setCreateId(tokenService.getUserId(request, response));
                permission.setCreateTime(LocalDateTime.now());
                if (permissionService.save(permission)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, permission);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, permission);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param permission
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Permission permission, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), permission);
        } else {
            if (null == permissionService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, permission);
            } else {
                permission.setPerId(id);
                permission.setUpdateId(tokenService.getUserId(request, response));
                permission.setUpdateTime(LocalDateTime.now());
                if (permissionService.updateById(permission)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, permission);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, permission);
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
        if (null == permissionService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (permissionService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

    /**
     * 查询本人拥有的权限树
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/findCurrMenuTree")
    public ReturnEntity findMenuByUserId(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long userId = tokenService.getUserId(request, response);
            List<MenuVO> menuVOList = this.permissionService.findMenuByUserId(userId);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, menuVOList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

    /**
     * 查询本人的权限列表
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/findListByUserId")
    public ReturnEntity findListByUserId(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long userId = tokenService.getUserId(request, response);
            List<Permission> permissionList = this.permissionService.findListByUserId(userId);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

    /**
     * 根据roleId获取权限列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/findListByRoleId/{roleId}")
    public ReturnEntity findListByRoleId(@PathVariable("roleId") Long roleId) {
        try {
            List<Permission> permissionList = this.permissionService.findListByRoleId(roleId);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED);
        }
    }

}
