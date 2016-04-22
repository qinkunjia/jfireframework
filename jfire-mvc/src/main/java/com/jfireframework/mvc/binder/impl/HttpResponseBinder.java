package com.jfireframework.mvc.binder.impl;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfireframework.mvc.binder.AbstractDataBinder;

public class HttpResponseBinder extends AbstractDataBinder
{
    
    public HttpResponseBinder(String paramName)
    {
        super(paramName);
    }
    
    @Override
    public Object binder(HttpServletRequest request, Map<String, String> map, HttpServletResponse response)
    {
        return response;
    }
    
}