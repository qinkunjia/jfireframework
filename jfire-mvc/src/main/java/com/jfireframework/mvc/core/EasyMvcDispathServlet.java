package com.jfireframework.mvc.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.baseutil.simplelog.ConsoleLogFactory;
import com.jfireframework.baseutil.simplelog.Logger;
import com.jfireframework.mvc.core.action.Action;
import com.jfireframework.mvc.util.ChangeMethodRequest;

/**
 * 充当路径分发器的类，用来根据地址规则转发数据请求
 * 
 * @author 林斌（eric@jfire.cn）
 * 
 */
@WebServlet(name = "EasyMvcDispathServlet", value = "/*", loadOnStartup = 1, asyncSupported = true)
@MultipartConfig
public class EasyMvcDispathServlet extends HttpServlet
{
    /**
     * 
     */
    private static final long    serialVersionUID      = 6091581255799463902L;
    private Logger               logger                = ConsoleLogFactory.getLogger();
    private DispathServletHelper helper;
    private String               encode;
    private static final String  DEFAULT_METHOD_PREFIX = "_method";
    private ResourcesHandler     resourcesHandler;
    
    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
        logger.debug("初始化Context-mvc Servlet");
        helper = new DispathServletHelper(servletConfig);
        encode = helper.encode();
        Set<String> set = new HashSet<String>();
        set.add("img/");
        set.add("classpath:i:img/");
        resourcesHandler = new ResourcesHandler(servletConfig.getServletContext().getContextPath(), set);
    }
    
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
    {
        helper.preHandleDevMode();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        request.setCharacterEncoding(encode);
        response.setCharacterEncoding(encode);
        if (request.getMethod().equals("POST") && StringUtil.isNotBlank(request.getParameter(DEFAULT_METHOD_PREFIX)))
        {
            String method = request.getParameter(DEFAULT_METHOD_PREFIX).toUpperCase();
            request = new ChangeMethodRequest(method, request);
        }
        if (resourcesHandler.handle(request, response))
        {
            return;
        }
        Action action = helper.getAction(request);
        if (action == null)
        {
            helper.handleStaticResourceRequest(request, response);
            return;
        }
        try
        {
            action.render(request, response);
        }
        catch (Throwable e)
        {
            logger.error("访问action出现异常,action为{}", action.getRequestUrl(), e);
            response.sendError(500, e.getLocalizedMessage());
        }
    }
}
