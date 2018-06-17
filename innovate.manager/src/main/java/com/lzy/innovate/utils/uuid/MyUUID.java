package com.lzy.innovate.utils.uuid;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by laizhiyuan on 2017/3/17.
 */
public abstract class MyUUID {

    protected static boolean IS_THREADLOCALRANDOM_AVAILABLE = false;
    protected static Random random;
    protected static final long   leastSigBits;
    protected static final ReentrantLock lock = new ReentrantLock();
    protected static long lastTime;

    static {
        try {
            IS_THREADLOCALRANDOM_AVAILABLE = null != MyUUID.class.getClassLoader().loadClass(
                    "java.util.concurrent.ThreadLocalRandom"
            );
        } catch(ClassNotFoundException e) {
        }

        byte[] seed = new SecureRandom().generateSeed(8);
        leastSigBits = new BigInteger(seed).longValue();
        if(!IS_THREADLOCALRANDOM_AVAILABLE) {
            random = new Random(leastSigBits);
        }
    }

    MyUUID() {
    }

    /**
     * 抽象方法
     * @return
     */
    public abstract String generateUuidByByte();

    /**
     * 抽象方法
     * @return
     */
    public abstract String generateUuidByTime();
}
