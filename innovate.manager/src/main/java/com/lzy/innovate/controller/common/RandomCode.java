package com.lzy.innovate.controller.common;

import java.util.Random;

/**
 * Created by lzy on 2017/3/8.
 */
public class RandomCode {

    public static void main(String[] args){
       // String s = SaltEncoder.md5SaltEncode("admin", "123456");
        int random = new Random().nextInt(10000);
        int random2 = new Random().nextInt(1000);
        System.out.println(random + random2);
    }
}
