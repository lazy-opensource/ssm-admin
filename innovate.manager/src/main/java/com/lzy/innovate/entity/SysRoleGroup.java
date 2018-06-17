package com.lzy.innovate.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("sys_role_group")
public class SysRoleGroup extends Model<SysRoleGroup> {

    private static final long serialVersionUID = 1L;

	@TableId
	private String uuid;
	@TableField("role_uuid")
	private String roleUuid;
	@TableField("group_uuid")
	private String groupUuid;


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

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}

}
