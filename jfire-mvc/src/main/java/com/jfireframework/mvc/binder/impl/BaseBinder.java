package com.jfireframework.mvc.binder.impl;

import java.lang.annotation.Annotation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.baseutil.exception.UnSupportException;
import com.jfireframework.mvc.binder.DataBinder;
import com.jfireframework.mvc.binder.node.ParamNode;
import com.jfireframework.mvc.binder.node.StringValueNode;
import com.jfireframework.mvc.binder.node.TreeValueNode;
import com.jfireframework.mvc.binder.transfer.Transfer;
import com.jfireframework.mvc.binder.transfer.TransferFactory;

public class BaseBinder implements DataBinder
{
    
    private final Transfer<?> transfer;
    private final String      prefixName;
    
    public BaseBinder(Class<?> ckass, String prefixName, Annotation[] annotations)
    {
        this.prefixName = prefixName;
        transfer = TransferFactory.build(ckass);
    }
    
    @Override
    public Object bind(HttpServletRequest request, TreeValueNode treeValueNode, HttpServletResponse response)
    {
        ParamNode node = treeValueNode.get(prefixName);
        if (node == null)
        {
            throw new UnSupportException(StringUtil.format("参数为基本类型，页面必须要有传参，请检查传参名字是否是{}", prefixName));
        }
        return transfer.trans(((StringValueNode) node).getValue());
    }
    
    @Override
    public String getParamName()
    {
        return prefixName;
    }
    
}
