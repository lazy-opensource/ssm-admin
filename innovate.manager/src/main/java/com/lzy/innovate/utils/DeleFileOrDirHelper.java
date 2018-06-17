package com.lzy.innovate.utils;

import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.io.File;

/**
 * Created by lzy on 2017/2/28.
 */
public class DeleFileOrDirHelper {


    public static boolean delFile(String path){
        if (StringUtils.isEmpty(path)){
            return false;
        }

        File f = new File(path);
        if (f.exists()){
            f.delete();
            return true;
        }
        return false;
    }

    public static boolean delDir(String path){
        if (StringUtils.isEmpty(path)){
            return false;
        }

        File file = new File(path);
        File[] files = file.listFiles();
        boolean tag = true;

        if (files == null){
            return false;
        }
        for (File f : files){
            System.out.println("0000000000000000000000000000" + f.getAbsoluteFile());
            if (f.isDirectory()){

                boolean b  = delDir(f.getAbsolutePath());
                if (!b){
                    break;
                }
            }
            boolean bb = delFile(f.getAbsolutePath());
            if (!bb){
                break;
            }
        }

        return tag;
    }
}
