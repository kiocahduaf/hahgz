
package com.jswhzl.common.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Entity基类
 *
 * @param <T>
 * @author xuchao
 */
public class BaseEntity<T extends BaseEntity<?>> extends Model<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 设置主键
     */
    @Override
    protected Serializable pkVal() {
        Long value = null;
        List<Field> list = Arrays.asList(this.getClass().getDeclaredFields());
        for (int i = 0; i < list.size(); i++) {
            Field field = list.get(i);
            //是否使用TableId注解
            if (field.isAnnotationPresent(TableId.class)) {
                //System.out.println("实体类存在" + list.size() + "个变量，字段名" + field.getName() + "有" + field.getDeclaredAnnotations().length + "个注解（包括tableId）");
                try {
                    String id = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    // 父类对象调用子类方法(反射原理)
                    Method method = this.getClass().getMethod("get" + id);
                    Object o = method.invoke(this);
                    value = Long.valueOf(o.toString());
                } catch (Exception e) {
                    //System.out.println("pkval()有异常");
                }
            }
        }
        return value;
    }

    /**
     * 设置主键
     * @return Serializable 主键
     */
	/*@Override
	public  Serializable pkVal(){
		Long value=null;
		List<Field> list = Arrays.asList(this.getClass().getDeclaredFields());
		for (int i = 0; i < list.size(); i++) {
			Field field = list.get(i);
			//是否使用TableId注解
			if (field.isAnnotationPresent(TableId.class)) {
				System.out.println("实体类存在" + list.size() + "个变量，字段名" + field.getName() + "有" + field.getDeclaredAnnotations().length + "个注解（包括tableId）");
				try {
					String  id = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
					// 父类对象调用子类方法(反射原理)
					Method method = this.getClass().getMethod("get"+id);
					Object o = method.invoke(this);
					value=Long.valueOf(o.toString());
				} catch (Exception e) {
					System.out.println("pkval()有异常");
				}
			}
		}
		return value;

	};*/


}
