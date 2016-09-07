package com.jfireframework.context.event;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public final class ApplicationEvent
{
    private final Object     data;
    private final Enum<?>    type;
    private volatile boolean finished = false;
    private Thread           owner;
    private volatile boolean await    = false;
    
    public ApplicationEvent(Object data, Enum<?> type)
    {
        this.data = data;
        this.type = type;
    }
    
    /**
     * 等待直到该事件被处理完成
     */
    public void await()
    {
        while (finished == false)
        {
            owner = Thread.currentThread();
            await = true;
            if (finished == false)
            {
                LockSupport.park();
            }
            else
            {
                break;
            }
        }
    }
    
    /**
     * 完成该事件，唤醒等待该事件的线程
     */
    public void signal()
    {
        finished = true;
        if (await)
        {
            LockSupport.unpark(owner);
        }
    }
    
    public void await(long mills)
    {
        long left = TimeUnit.MILLISECONDS.toNanos(mills);
        while (finished == false)
        {
            owner = Thread.currentThread();
            await = true;
            if (finished == false)
            {
                long t0 = System.nanoTime();
                LockSupport.parkNanos(left);
                long t1 = System.nanoTime();
                left -= t1 - t0;
                // 1000纳秒其实非常短，就不要再等待了
                if (left < 1000)
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
    }
    
    public Object getData()
    {
        return data;
    }
    
    public Enum<?> getType()
    {
        return type;
    }
    
}
