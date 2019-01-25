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
import com.jswhzl.api.service.impl.AttachmentServiceImpl;
import com.jswhzl.api.entity.Attachment;
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
@RequestMapping("/api/attachment")
public class AttachmentController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    @Autowired
    private AttachmentServiceImpl attachmentService;

    /**
     * 分页查询列表
     *
     * @param pager 分页信息
     * @param attachment
     * @return
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Pager<Attachment> pager, Attachment attachment,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Attachment> wrapper = new QueryWrapper<>();
            wrapper.eq(null != attachment.getSourceId(), "sourceId", attachment.getSourceId());
            wrapper.eq(null != attachment.getType(), "type", attachment.getType());
            List<Attachment> records = attachmentService.page(pager, wrapper).getRecords();
            int count = attachmentService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(Constants.MSG_FIND_FAILED, null, null);
        }
    }

    /**
     * 查询所有列表
     * @param attachment
     * @return
     */
    @GetMapping
    public ReturnEntity selectList(Attachment attachment,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Attachment> wrapper = new QueryWrapper<>();
            wrapper.eq(null != attachment.getSourceId(), "sourceId", attachment.getSourceId());
            wrapper.eq(null != attachment.getType(), "type", attachment.getType());
            List<Attachment> list = attachmentService.list(wrapper);
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
            Attachment attachment = attachmentService.getById(id);
            if (null != attachment) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, attachment);
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
     * @param attachment
     * @param result
     * @return
     */
    @PostMapping
    public ReturnEntity save(@Validated @RequestBody Attachment attachment, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if(Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)){
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, attachment);
        } else {
            QueryWrapper<Attachment> wrapper = new QueryWrapper<>();
            if (attachmentService.count(wrapper) > 0) {
                return new ReturnEntityError(Constants.MSG_FIND_EXISTED, attachment);
            } else {
                if (attachmentService.save(attachment)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, null, attachment);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, null, attachment);
                }
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param attachment
     * @param result
     * @return
     */
    @PutMapping("/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Attachment attachment, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), attachment);
        } else {
            if (null == attachmentService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, attachment);
            } else {
                if (attachmentService.updateById(attachment)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, attachment);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, attachment);
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
        if (null == attachmentService.getById(id)) {
            return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
        }else{
            if (attachmentService.removeById(id)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
            }else{
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
            }
        }
    }

}
