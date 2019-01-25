/**
 * Copyright (C), 2015-2018, 江苏物合智联科技有限公司
 * FileName: MenuVO
 * Author:   xuchao
 * Date:     2018/11/16 0016 16:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jswhzl.common.vo;

import com.jswhzl.common.base.BaseBean;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * Description: 
 *
 * @author xuchao
 * @create 2018/11/16 0016 16:10
 * @since 1.0.0
 */
@Data
public class MenuVO extends BaseBean {

    private static final long serialVersionUID = -5231588779879799761L;

    /**
     * 菜单名称（与视图的文件夹名称和路由路径对应）
     */
    private String name;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标样式
     */
    private String icon;

    /**
     * 自定义菜单路由地址，默认按照 name 解析。一旦设置，将优先按照 jump 设定的路由跳转
     */
    private String jump;

    /**
     * 是否默认展子菜单
     */
    private Boolean spread;

    /**
     * 子菜单列表
     */
    private List<MenuVO> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getJump() {
		return jump;
	}

	public void setJump(String jump) {
		this.jump = jump;
	}

	public Boolean getSpread() {
		return spread;
	}

	public void setSpread(Boolean spread) {
		this.spread = spread;
	}

	public List<MenuVO> getList() {
		return list;
	}

	public void setList(List<MenuVO> list) {
		this.list = list;
	}
    
    
}
