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
 * @since 2017-02-27
 */
@TableName("sys_role_oper")
public class SysRoleOper extends Model<SysRoleOper> {

    private static final long serialVersionUID = 1L;

	private String uuid;
	@TableField("role_uuid")
	private String roleUuid;
	@TableField("oper_uuid")
	private String operUuid;


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
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
