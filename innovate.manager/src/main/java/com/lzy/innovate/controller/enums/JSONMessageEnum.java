package com.lzy.innovate.controller.enums;

/**
 * Created by laizhiyuan on 2017/3/18.
 * 前台参数错误
 */
public enum JSONMessageEnum {

    OPER_SUCCESS("操作成功!"),OPER_FAILD("操作失败！"),PARAM_ERROR("操作失败，参数错误!"),PARAM_EMPTY("操作失败,参数为空！"),
    SYSTEM_EXCEPTION("操作失败，系统异常,请尽快联系管理员!"),SUCCESS("success"),FAILD("faild"),NOT_LEGAL("登录用户不合法！"),NOT_CONTACT_GROUP("请先为该角色关联组！"),
    NOT_PERMISSION("没有操作权限，请联系管理员!");

    private String message;
    private JSONMessageEnum(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
