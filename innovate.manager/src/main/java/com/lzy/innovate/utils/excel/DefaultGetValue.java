package com.lzy.innovate.utils.excel;

/**
 * Created by Dell on 2017/6/5.
 */
public class DefaultGetValue extends AbstractGetFieldValue {

    @Override
    public Object getFiledValue(Object obj, String fieldName) {

        if (fieldName == null){
            return "";
        }

        return ReflectingHelper.getValueByField(obj, fieldName.toString());
    }
}
