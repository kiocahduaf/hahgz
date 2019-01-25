/**
 * Copyright (C), 2015-2018, 江苏物合智联科技有限公司
 * FileName: UserVO
 * Author:   xuchao
 * Date:     2018/11/16 0016 10:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jswhzl.common.vo;

import com.jswhzl.api.entity.Role;
import com.jswhzl.api.entity.User;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * Description:
 *
 * @author xuchao
 * @create 2018/11/16 0016 10:34
 * @since 1.0.0
 */
@Data
public class UserVO {

    private User user;

    private Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
