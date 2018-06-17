package com.lzy.innovate.exception;

/**
 * Created by lzy on 2017/3/12.
 * web 异常类
 */
public class WebException extends RuntimeException {

    public WebException(){
        super();
    }

    public WebException(String msg){
        super(msg);
    }
}
