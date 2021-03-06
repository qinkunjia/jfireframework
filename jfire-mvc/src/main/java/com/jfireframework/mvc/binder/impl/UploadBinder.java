package com.jfireframework.mvc.binder.impl;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfireframework.mvc.binder.DataBinder;
import com.jfireframework.mvc.binder.node.TreeValueNode;
import com.jfireframework.mvc.interceptor.impl.UploadInterceptor;

public class UploadBinder implements DataBinder
{
    private final String prefixName;
    
    public UploadBinder(Class<?> ckass, String prefixName, Annotation[] annotations)
    {
        this.prefixName = prefixName;
    }
    
    @Override
    public Object bind(HttpServletRequest request, TreeValueNode treeValueNode, HttpServletResponse response)
    {
        Object value = request.getAttribute(UploadInterceptor.uploadFileList);
        if (value == null)
        {
            return null;
        }
        else
        {
            if (((List<?>) value).size() == 0)
            {
                return null;
            }
            else
            {
                return ((List<?>) value).get(0);
            }
        }
    }
    
    @Override
    public String getParamName()
    {
        return prefixName;
    }
    
}
