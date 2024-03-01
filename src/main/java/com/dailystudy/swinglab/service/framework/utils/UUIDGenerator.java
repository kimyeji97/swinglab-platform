package com.dailystudy.swinglab.service.framework.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDGenerator
{
    private static final Object MUTEX = new Object();

    public static final String get()
    {
        synchronized (MUTEX)
        {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * @return Assurance Global Transaction ID
     */
    public static long generateTxid()
    {
        return ByteBuffer.wrap(UUIDGenerator.get().getBytes()).asLongBuffer().get();
    }
    public static long buildTXID(String uuid)
    {
        return ByteBuffer.wrap(uuid.getBytes()).asLongBuffer().get();
    }
}
