/**
 * Copyright (C), 2015-2018, 江苏物合智联科技有限公司
 * FileName: UserDTO
 * Author:   xuchao
 * Date:     2018/11/16 0016 15:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jswhzl.common.dto;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jswhzl.api.entity.User;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * Description: 
 *
 * @author xuchao
 * @create 2018/11/16 0016 15:53
 * @since 1.0.0
 */
@Data
public class PermissionDTO extends User {
    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
	private List<Long> perIds;
	public List<Long> getPerIds() {
		return perIds;
	}
	public void setPerIds(List<Long> perIds) {
		this.perIds = perIds;
	}
    
    
    
}
