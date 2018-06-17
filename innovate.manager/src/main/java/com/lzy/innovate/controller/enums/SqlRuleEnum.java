package com.lzy.innovate.controller.enums;

import org.springframework.util.StringUtils;

/**
 * Created by laizhiyuan on 2017/3/13.
 */
public enum SqlRuleEnum {


    LT("LT"," < "),GT("GT"," > "),LE("LE"," <= "),GE("GE"," >= "),LIKE("LIKE"," like "),EQ("EQ"," = ");

    private String rule;
    private String key;

    private SqlRuleEnum(String key, String rule){
        this.rule = rule;
        this.key = key;
    }

    public static String getRule(String key){

        if (StringUtils.isEmpty(key)){
            return EQ.getRule();
        }

        for (SqlRuleEnum sqlRuleEnum : SqlRuleEnum.values()){
            if (key.equals(sqlRuleEnum.getKey())){
                return sqlRuleEnum.getRule();
            }
        }

        return EQ.getRule();
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
