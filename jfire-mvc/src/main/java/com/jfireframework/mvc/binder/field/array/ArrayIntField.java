package com.jfireframework.mvc.binder.field.array;

import java.lang.reflect.Field;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfireframework.baseutil.StringUtil;

public class ArrayIntField extends AbstractArrayField
{
    
    public ArrayIntField(String prefix, Field field)
    {
        super(prefix, field);
    }
    
    @SuppressWarnings("restriction")
    @Override
    public Object setValue(HttpServletRequest request, Object entity, Map<String, String> map, HttpServletResponse response) throws InstantiationException, IllegalAccessException
    {
        int[] array = null;
        if (entity != null)
        {
            array = (int[]) unsafe.getObject(entity, offset);
        }
        String value = null;
        for (int i = 0; i < length; i++)
        {
            value = map.get(requestParamNames[i]);
            if (StringUtil.isNotBlank(value))
            {
                if (entity == null)
                {
                    entity = type.newInstance();
                    array = (int[]) unsafe.getObject(entity, offset);
                }
                array[i] = Integer.valueOf(value);
            }
        }
        return entity;
    }
    
}