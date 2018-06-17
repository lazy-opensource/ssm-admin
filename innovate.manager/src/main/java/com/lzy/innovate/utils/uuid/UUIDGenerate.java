package com.lzy.innovate.utils.uuid;

/**
 * Created by laizhiyuan on 2017/3/17.
 * UUID生成器
 */
public class UUIDGenerate {

    private static MyUUID myUuid;


    /**
     * 可自定义实现类
     * @param myUuid
     */
    public static void setUuidImpl(MyUUID myUuid) {
        UUIDGenerate.myUuid = myUuid;
    }

    /**
     * 生成UUID by time
     * @return
     */
    public static String generateUuidByTime(){

        check();
        return myUuid.generateUuidByTime();
    }

    /**
     * 生成uuid by byte
     * @return
     */
    public static String generateUuidByByte(){

        check();
        return myUuid.generateUuidByByte();
    }

    /**
     * 检查是否注入自定义实现
     */
    private static void check(){
        if (myUuid == null){
            myUuid = new DefaultMyUUIDImpl();
        }
    }
}
