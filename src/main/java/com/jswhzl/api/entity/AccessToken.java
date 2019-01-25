package com.jswhzl.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.jswhzl.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuchao
 * @since 2019-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_access_token")
public class AccessToken extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String accessToken;

    /**
     * 有效秒数
     */
    private Integer expiresIn;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 失效时间
     */
    @TableField("expiresTime")
    private LocalDateTime expiresTime;

    /**
     * 错误码
     * -1	    系统繁忙，此时请开发者稍候再试
     * 0	    请求成功
     * 40001	AppSecret 错误或者 AppSecret 不属于这个小程序，请开发者确认 AppSecret 的正确性
     * 40002	请确保 grant_type 字段值为 client_credential
     * 40013	不合法的 AppID，请开发者检查 AppID 的正确性，避免异常字符，注意大小写
     */
    @TableField(exist = false)
    private Integer errcode;

    /**
     * 错误信息
     */
    @TableField(exist = false)
    private String errmsg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(LocalDateTime expiresTime) {
		this.expiresTime = expiresTime;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
    
    
}
