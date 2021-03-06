package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.Licp;
import com.jfireframework.licp.util.BufferUtil;

public class WShortArraySerializer extends AbstractArraySerializer
{
    
    public WShortArraySerializer()
    {
        super(Short[].class);
    }
    
    @Override
    public void serialize(Object src, ByteBuf<?> buf, Licp licp)
    {
        Short[] array = (Short[]) src;
        buf.writePositive(array.length);
        for (Short each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeShort(each);
            }
        }
    }
    
    @Override
    public Object deserialize(ByteBuf<?> buf, Licp licp)
    {
        int length = buf.readPositive();
        Short[] array = new Short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readShort();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Object deserialize(ByteBuffer buf, Licp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Short[] array = new Short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readShort(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
}
