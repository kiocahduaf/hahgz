package com.jswhzl.api.controller;

import java.io.FileOutputStream;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.jswhzl.api.entity.Attachment;
import com.jswhzl.api.service.TokenService;
import com.jswhzl.common.base.BaseController;
import com.jswhzl.common.bean.ReturnEntity;
import com.jswhzl.common.bean.ReturnEntityError;
import com.jswhzl.common.bean.ReturnEntitySuccess;
import com.jswhzl.common.util.FileUploadUtil;

import cn.hutool.crypto.SecureUtil;

/**
 * @author xuchao
 * @ClassName ApiUploadController
 * @Description 上传接口
 * @date 2018-06-12 8:32
 **/
@RestController
@RequestMapping("/api")
public class UploadController extends BaseController {
    private static final long serialVersionUID = 6087390561442587655L;
    private static Logger logger = LoggerFactory.getLogger(UploadController.class.getName());
    private static final String UPLOADFILE_TYPE_IMG = "img";
    private static final String UPLOADFILE_TYPE_VIDEO = "video";
    private static final String UPLOADFILE_TYPE_AUDIO = "audio";
    private static final String UPLOADFILE_TYPE_EXCEL = "excel";
    private static final String UPLOADFILE_TYPE_WORD = "word";
    private static final String UPLOADFILE_TYPE_PDF = "pdf";
    private static final String UPLOADFILE_TYPE_PPT = "ppt";
    /**
     * 图片后缀
     */
    private static final String IMAGE_SUFFIX = "jpg|png|gif|bmp|jpeg";
    /**
     * 视频后缀
     */
    private static final String VIDEO_SUFFIX = "mp4|mov|webm|ogg";
    /**
     * 音频后缀
     */
    private static final String AUDIO_SUFFIX = "mp3|wav|ogg";
    /**
     * excel后缀
     */
    private static final String EXCEL_SUFFIX = "xls|xlsx|xlsm";
    /**
     * word后缀
     */
    private static final String WORD_SUFFIX = "doc|docx";
    /**
     * ppt后缀
     */
    private static final String PPT_SUFFIX = "ppt|pptx";
    /**
     * pdf后缀
     */
    private static final String PDF_SUFFIX = "pdf";

    @Autowired
    private TokenService tokenService;

    @PostMapping("/upload")
    public ReturnEntity uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "type", required = false) Integer type,
                                   @RequestParam(value = "sourceId", required = false) Long sourceId,
                                   @RequestParam(value = "fileType", required = false, defaultValue = "") String fileType,
                                   @RequestParam(value = "fileLength", required = false, defaultValue = "10") Integer fileLength,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
//            String realPath = request.getSession().getServletContext().getRealPath("/");
            String realPath = "/home/attachment/hahgz";
            String filePath = FileUploadUtil.createFileCatalog(realPath, "upload");
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            String url = filePath + SecureUtil.simpleUUID() + suffix;
            if ("".equals(fileType)) {
                if (IMAGE_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_IMG;
                } else if (VIDEO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_VIDEO;
                } else if (AUDIO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_AUDIO;
                } else if (EXCEL_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_EXCEL;
                } else if (WORD_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_WORD;
                } else if (PDF_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_PDF;
                } else if (PPT_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_PPT;
                } else {
                    fileType = "";
                }
            }

            if (file.isEmpty()) {
                return new ReturnEntityError("附件不能为空");
            } else if (file.getSize() > 1024 * 1024 * fileLength) {
                return new ReturnEntityError("附件大小超过限制");
            } else if (!FileUploadUtil.checkFileSuffix(fileType, originalFileName.substring(originalFileName.lastIndexOf(".") + 1))) {
                return new ReturnEntityError("附件格式错误");
            } else {
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realPath + url));
                Attachment attachment = new Attachment();
                attachment.setName(originalFileName);
                attachment.setType(null == type ? 0 : type);
                attachment.setUrl(url);
                if (null != sourceId) {
                    attachment.setSourceId(sourceId);
                }
                if (StringUtils.isNotBlank(request.getHeader("Authorization"))) {
                    attachment.setCreateId(tokenService.getUserId(request, response));
                } else {
                    attachment.setCreateId(1L);
                }
                attachment.setCreateTime(LocalDateTime.now());
                if (attachment.insert()) {
                    return new ReturnEntitySuccess("上传成功", attachment);
                } else {
                    return new ReturnEntityError("上传失败");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnEntityError("上传失败");
        }
    }

    @PostMapping("/articleUpload")
    public ReturnEntity articleUpload(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "type", required = false) Integer type,
                                      @RequestParam(value = "sourceId", required = false) Long sourceId) {
        try {
//            String realPath = request.getSession().getServletContext().getRealPath("/");
            String realPath = "/home/attachment/hahgz";
            String filePath = FileUploadUtil.createFileCatalog(realPath, "upload");
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            String url = filePath + SecureUtil.simpleUUID() + suffix;

            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realPath + url));
            Attachment attachment = new Attachment();
            attachment.setName(originalFileName);
            attachment.setType(null == type ? 0 : type);
            attachment.setUrl(url);
            if (null != sourceId) {
                attachment.setSourceId(sourceId);
            }
            attachment.setCreateId(1L);
            attachment.setCreateTime(LocalDateTime.now());

            if (attachment.insert()) {
                JSONObject object = new JSONObject();
                object.put("src", "http://localhost:8080/file" + attachment.getUrl());
                object.put("title", attachment.getName());
                return new ReturnEntitySuccess("上传成功", object);
            } else {
                return new ReturnEntityError("上传失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnEntityError("上传失败");
        }
    }

    /**
     * 上传附件，返回URL
     *
     * @param file
     * @param type
     * @return
     */
    @PostMapping("/uploadWithURL")
    public ReturnEntity uploadWithURL(@RequestParam("file") MultipartFile file, @RequestParam(value = "type", required = false) Integer type) {
        try {
            String realPath = "/home/attachment/hahgz";
            String filePath = FileUploadUtil.createFileCatalog(realPath, "upload");
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            String url = filePath + SecureUtil.simpleUUID() + suffix;

            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realPath + url));
            Attachment attachment = new Attachment();
            attachment.setName(originalFileName);
            attachment.setType(null == type ? 0 : type);
            attachment.setUrl(url);
            attachment.setCreateId(1L);
            attachment.setCreateTime(LocalDateTime.now());

            if (attachment.insert()) {
                return new ReturnEntitySuccess("上传成功", attachment.getUrl());
            } else {
                return new ReturnEntityError("上传失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnEntityError("上传失败");
        }
    }
}
