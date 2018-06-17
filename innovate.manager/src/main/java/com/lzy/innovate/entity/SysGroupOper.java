package com.lzy.innovate.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author laizy
 * @since 2017-03-15
 */
@TableName("sys_group_oper")
public class SysGroupOper extends Model<SysGroupOper> {

    private static final long serialVersionUID = 1L;

	@TableField
	private String uuid;
	@TableField("group_uuid")
	private String groupUuid;
	@TableField("oper_uuid")
	private String operUuid;


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getOperUuid() {
		return operUuid;
	}

	public void setOperUuid(String operUuid) {
		this.operUuid = operUuid;
	}

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}

}
