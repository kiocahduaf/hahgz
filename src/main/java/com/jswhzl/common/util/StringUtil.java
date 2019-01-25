/**
 *
 */
package com.jswhzl.common.util;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author JinJichao
 */
public class StringUtil extends StringUtils {

    /**
     *
     */
    public static final String EMPTY = "";

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String... array) {
        boolean flag = false;
        if (array != null && array.length > 0) {
            for (String str : array) {
                flag = StringUtil.isEmpty(str);
                if (flag) {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 判断Collection是否为空
     *
     * @param collect
     * @return
     */
    public static boolean isEmpty(Collection<?> collect) {
        return collect == null || collect.size() == 0;
    }

    /**
     * 判断Collection是否不为空
     *
     * @param collect
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collect) {
        return !StringUtil.isEmpty(collect);
    }

    /**
     * 判断Map是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * 判断Map是否不为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !StringUtil.isEmpty(map);
    }

    /**
     * 把首字母转为小写
     *
     * @param str
     * @return
     */
    public static String convertFirstCharToLower(String str) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(str)) {
            String firstChar = str.substring(0, 1);
            String firstCharLower = firstChar.toLowerCase();
            return firstCharLower + str.substring(1);
        } else {
            throw new RuntimeException("字符串不能为空");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

}
