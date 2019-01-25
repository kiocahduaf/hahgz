package com.jswhzl.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuchao
 * @ClassName ReturnEntity
 * @Description
 * @date 2018-10-17 9:08
 **/
@Data
public class ReturnEntity implements Serializable {
    private static final long serialVersionUID = 1275584334145317101L;

    private Integer code;

    private String msg;

    private Integer count;

    private Object data;

    public ReturnEntity() {
    }

    public ReturnEntity(Integer code, String msg, Integer count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    

}
