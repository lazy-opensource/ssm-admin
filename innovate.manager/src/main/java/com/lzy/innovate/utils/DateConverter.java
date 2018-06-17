package com.lzy.innovate.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by laizhiyuan on 2017/3/15.
 */
public class DateConverter implements Converter<String, Date> {

    private Logger logger = LoggerFactory.getLogger("common_framework_log");

    @Override
    public Date convert(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return (Date) simpleDateFormat.parse(s);
        } catch (ParseException e) {
            logger.error("data converter error");
            e.printStackTrace();
        }
        return null;
    }
}
