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
@TableName("sys_group_menu")
public class SysGroupMenu extends Model<SysGroupMenu> {

    private static final long serialVersionUID = 1L;

	private String uuid;
	@TableField("group_uuid")
	private String groupUuid;
	@TableField("menu_uuid")
	private String menuUuid;


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

	public String getMenuUuid() {
		return menuUuid;
	}

	public void setMenuUuid(String menuUuid) {
		this.menuUuid = menuUuid;
	}

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}

}
