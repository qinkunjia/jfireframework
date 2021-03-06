package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.Licp;
import com.jfireframework.licp.util.BufferUtil;

public class ShortField extends AbstractCacheField
{
    
    public ShortField(Field field)
    {
        super(field);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, Licp licp)
    {
        short value = unsafe.getShort(holder, offset);
        buf.writeShort(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, Licp licp)
    {
        unsafe.putShort(holder, offset, buf.readShort());
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, Licp licp)
    {
        unsafe.putShort(holder, offset, BufferUtil.readShort(buf));
    }
    
}
