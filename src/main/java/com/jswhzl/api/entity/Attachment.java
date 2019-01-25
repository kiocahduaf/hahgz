package com.jswhzl.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jswhzl.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuchao
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_attachment")
public class Attachment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 地址url
     */
    private String url;

    /**
     * 简述
     */
    private String des;

    /**
     * 附件类型
     * 1：房屋安全鉴定申请表；
     * 2：产权证件；
     * 3：房屋为租赁需提供租赁合同；
     * 4：建筑、结构图纸；
     * 5：新建工程竣工资料；
     * 6：有资质的检测机构出具的检测报告；
     * 7：鉴定方案；
     * 8：检测方案；
     * 9：检测报告；
     * 10：鉴定报告；
     * 11：房屋现状图
     * 12：现场实施附件
     */
    private Integer type;

    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("createId")
    private Long createId;

    /**
     * 来源ID
     */
    @TableField("sourceId")
    private Long sourceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
    
    

}
