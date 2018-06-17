package com.lzy.innovate.utils.uuid;

import java.util.UUID;

/**
 * Created by laizhiyuan on 2017/3/17.
 */
public class DefaultMyUUIDImpl extends MyUUID {


    public DefaultMyUUIDImpl() {
        super();
    }

    @Override
    public String generateUuidByByte() {
        return getUuidByByte().toString();
    }

    @Override
    public String generateUuidByTime() {
        return getUuidByTime().toString();
    }


    /**
     * Create a new random UUID.
     *
     * @return the new UUID
     */
    private UUID getUuidByByte() {
        byte[] randomBytes = new byte[16];
        if(IS_THREADLOCALRANDOM_AVAILABLE) {
            java.util.concurrent.ThreadLocalRandom.current().nextBytes(randomBytes);
        } else {
            random.nextBytes(randomBytes);
        }

        long mostSigBits = 0;
        for(int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (randomBytes[i] & 0xff);
        }
        long leastSigBits = 0;
        for(int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (randomBytes[i] & 0xff);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * Create a new time-based UUID.
     *
     * @return the new UUID
     */
    public UUID getUuidByTime() {
        long timeMillis = (System.currentTimeMillis() * 10000) + 0x01B21DD213814000L;

        lock.lock();
        try {
            if(timeMillis > lastTime) {
                lastTime = timeMillis;
            } else {
                timeMillis = ++lastTime;
            }
        } finally {
            lock.unlock();
        }

        // time low
        long mostSigBits = timeMillis << 32;

        // time mid
        mostSigBits |= (timeMillis & 0xFFFF00000000L) >> 16;

        // time hi and version
        mostSigBits |= 0x1000 | ((timeMillis >> 48) & 0x0FFF); // version 1

        return new UUID(mostSigBits, leastSigBits);
    }



}
