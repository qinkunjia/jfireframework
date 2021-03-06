package com.jfireframework.licp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 对象类型序号注册中心。对象类型序号从2开始。
 * 
 * @author linbin
 *
 */
public class ClassNoRegister
{
    private final IdentityHashMap<Class<?>, Integer> originMap = new IdentityHashMap<Class<?>, Integer>();
    private final int                                originSequence;
    private int                                      sequence;
    private Class<?>[]                               types     = new Class[300];
    
    private List<Class<?>> pre(Class<?>... types)
    {
        List<Class<?>> tmp = new ArrayList<Class<?>>();
        tmp.add(boolean.class);
        tmp.add(int.class);
        tmp.add(short.class);
        tmp.add(char.class);
        tmp.add(long.class);
        tmp.add(byte.class);
        tmp.add(float.class);
        tmp.add(double.class);
        tmp.add(boolean[].class);
        tmp.add(int[].class);
        tmp.add(short[].class);
        tmp.add(char[].class);
        tmp.add(long[].class);
        tmp.add(byte[].class);
        tmp.add(float[].class);
        tmp.add(double[].class);
        tmp.add(boolean[][].class);
        tmp.add(int[][].class);
        tmp.add(short[][].class);
        tmp.add(char[][].class);
        tmp.add(long[][].class);
        tmp.add(byte[][].class);
        tmp.add(float[][].class);
        tmp.add(double[][].class);
        tmp.add(Integer.class);
        tmp.add(Short.class);
        tmp.add(Byte.class);
        tmp.add(Float.class);
        tmp.add(Long.class);
        tmp.add(Character.class);
        tmp.add(Double.class);
        tmp.add(Boolean.class);
        tmp.add(String.class);
        tmp.add(Integer[].class);
        tmp.add(Short[].class);
        tmp.add(Byte[].class);
        tmp.add(Float[].class);
        tmp.add(Long[].class);
        tmp.add(Character[].class);
        tmp.add(Double[].class);
        tmp.add(Boolean[].class);
        tmp.add(String[].class);
        tmp.add(Date.class);
        tmp.add(java.sql.Date.class);
        tmp.add(Calendar.class);
        tmp.add(ArrayList.class);
        tmp.add(LinkedList.class);
        tmp.add(HashMap.class);
        tmp.add(HashSet.class);
        tmp.add(Object.class);
        for (Class<?> each : types)
        {
            tmp.add(each);
        }
        return tmp;
    }
    
    public ClassNoRegister(Class<?>... outerRegisters)
    {
        List<Class<?>> list = pre(outerRegisters);
        int index = 1;
        for (Class<?> each : list)
        {
            originMap.put(each, index++);
        }
        originSequence = index;
        Class<?>[] tmpTypes = list.toArray(new Class<?>[list.size()]);
        if (types.length < tmpTypes.length + 200)
        {
            types = new Class<?>[tmpTypes.length + 200];
        }
        System.arraycopy(tmpTypes, 0, types, 1, tmpTypes.length);
        sequence = originSequence;
    }
    
    /**
     * 注册一个临时的类。如果该类已经注册过了，返回该类的序号。否则注册该类。并且返回0
     * 
     * @param type
     * @return
     */
    public int registerTemporary(Class<?> type)
    {
        Integer result = originMap.get(type);
        if (result == null)
        {
            for (int i = originSequence; i < sequence; i++)
            {
                if (types[i] == type)
                {
                    return i;
                }
            }
            if (sequence == types.length)
            {
                Class<?>[] tmp = new Class<?>[types.length << 1];
                System.arraycopy(types, 0, tmp, 0, types.length);
                types = tmp;
            }
            types[sequence++] = type;
            return 0;
        }
        else
        {
            return result;
        }
    }
    
    /**
     * 获取一个类型在类型注册中的顺序号,顺序号从1开始，如果不存在返回0.并且会将该类增加到系统中
     * 
     * @param type
     * @return
     */
    public int indexOf(Class<?> type)
    {
        return registerTemporary(type);
    }
    
    public Class<?> getType(int index)
    {
        return types[index];
    }
    
    public void clear()
    {
        sequence = originSequence;
    }
}
