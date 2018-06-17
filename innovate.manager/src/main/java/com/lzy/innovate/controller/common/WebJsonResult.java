package com.lzy.innovate.controller.common;

import com.lzy.innovate.controller.enums.JSONMessageEnum;
import com.lzy.innovate.utils.Sets;

import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/12.
 * 建议所有j返回son均用此类
 */
public class WebJsonResult {

    private String code;
    private String state = JSONMessageEnum.SUCCESS.getMessage();
    private String message = JSONMessageEnum.OPER_SUCCESS.getMessage();
    private Map<String, Object> map = Sets.map();
    private List<Object> list = Sets.list();
    private Object object;

    public WebJsonResult() {
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
