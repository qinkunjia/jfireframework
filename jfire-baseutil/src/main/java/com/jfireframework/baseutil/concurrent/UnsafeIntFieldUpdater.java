package com.jfireframework.baseutil.concurrent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.baseutil.verify.Verify;

import sun.misc.Unsafe;

public class UnsafeIntFieldUpdater<T>
{
    private final static Unsafe unsafe = ReflectUtil.getUnsafe();
    private final long          offset;
    
    public UnsafeIntFieldUpdater(Class<T> holderType, String fieldName)
    {
        try
        {
            Field field = holderType.getDeclaredField(fieldName);
            Verify.True(Modifier.isVolatile(field.getModifiers()), "属性必须是volatile修饰");
            Verify.True(field.getType() == int.class, "属性必须是int类型");
            offset = ReflectUtil.getFieldOffset(fieldName, holderType);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public boolean compareAndSwap(T holder, int excepted, int newValue)
    {
        return unsafe.compareAndSwapInt(holder, offset, excepted, newValue);
    }
    
    public int getAndIncrement(T holder)
    {
        return getAndAdd(holder, 1);
    }
    
    public int getAndAdd(T holder, int add)
    {
        do
        {
            int oldValue = unsafe.getIntVolatile(holder, offset);
            int newValue = oldValue + add;
            if (unsafe.compareAndSwapInt(holder, offset, oldValue, newValue))
            {
                return oldValue;
            }
        } while (true);
    }
}
