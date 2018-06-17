package com.lzy.innovate.utils;

/**
 * Created by laizhiyuan on 2017/3/13.
 */
public class KV {


    private String key;

    private String name;

    private Object value;

    public KV() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
