package com.jfireframework.litl.format.impl;

import java.text.DecimalFormat;
import com.jfireframework.litl.format.Format;

public class NumberFormat implements Format
{
    private ThreadLocal<DecimalFormat> formats;
    
    public NumberFormat(final String pattern)
    {
        formats = new ThreadLocal<DecimalFormat>() {
            protected DecimalFormat initialValue()
            {
                return new DecimalFormat(pattern);
            }
        };
    }
    
    @Override
    public String format(Object data)
    {
        return formats.get().format(data);
    }
    
}
