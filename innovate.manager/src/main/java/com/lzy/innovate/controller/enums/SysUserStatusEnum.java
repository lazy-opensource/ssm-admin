package com.lzy.innovate.controller.enums;

import com.lzy.innovate.utils.Sets;

import java.util.Map;

/**
 * Created by lzy on 2017/3/17.
 * 用户状态枚举
 */
public enum SysUserStatusEnum {

    ACTIVATE("激活","1"),ILLEGAL("不合法","2"),FREEZE("冻结","3"),STOP("停用" , "4");

    private SysUserStatusEnum(String describe,String status){
        this.status = status;
        this.describe = describe;
    }

    private String status;
    private String describe;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStatus() {
        return status;
    }

    public static Map<String, String> convertToMap(){
        Map<String, String> map = Sets.map();

        for (SysUserStatusEnum sysUserStatusEnum : SysUserStatusEnum.values()){
            map.put(sysUserStatusEnum.getDescribe(), sysUserStatusEnum.getStatus());
        }
        return map;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
