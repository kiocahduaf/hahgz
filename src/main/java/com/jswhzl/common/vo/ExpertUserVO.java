/**
 * Copyright (C), 2015-2018, 江苏物合智联科技有限公司
 * FileName: ExpertUserVO
 * Author:   xuchao
 * Date:     2018-12-29 11:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jswhzl.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jswhzl.api.entity.User;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * Description: 
 *
 * @author xuchao
 * @create 2018-12-29 11:03
 * @since 1.0.0
 */
@Data
public class ExpertUserVO extends User {

    private static final long serialVersionUID = -2710457277994156889L;

    @TableField(exist = false)
    private Integer expertType;

	public Integer getExpertType() {
		return expertType;
	}

	public void setExpertType(Integer expertType) {
		this.expertType = expertType;
	}
    
    
}
