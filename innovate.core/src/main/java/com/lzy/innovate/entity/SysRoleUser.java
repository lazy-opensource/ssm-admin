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
@TableName("sys_role_user")
public class SysRoleUser extends Model<SysRoleUser> {

    private static final long serialVersionUID = 1L;

	private String uuid;
	@TableField("role_uuid")
	private String roleUuid;
	@TableField("user_uuid")
	private String userUuid;


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

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}

}
