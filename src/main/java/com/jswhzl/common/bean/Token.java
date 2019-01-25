/**
 *
 */
package com.jswhzl.common.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.jswhzl.common.base.BaseBean;
import lombok.Data;

import java.util.Set;

/**
 * 令牌
 *
 * @author xuchao
 */
@Data
public class Token extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public static final String OBJECT_KEY = "TOKEN";

    /**
     * 用户Id
     */
    @TableId
    private Long userId;

    /**
     * token有效秒数
     */
    private Long expireSecond;

    /**
     * 资源列表
     */
    private Set<String> resSet;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getExpireSecond() {
		return expireSecond;
	}

	public void setExpireSecond(Long expireSecond) {
		this.expireSecond = expireSecond;
	}

	public Set<String> getResSet() {
		return resSet;
	}

	public void setResSet(Set<String> resSet) {
		this.resSet = resSet;
	}
    
    

}
