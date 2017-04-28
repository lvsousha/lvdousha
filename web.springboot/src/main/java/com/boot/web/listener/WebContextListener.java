package com.boot.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class WebContextListener implements ServletContextListener{
	

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("ServletContex初始化");
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        log.info(springContext.toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContex销毁");
    }
}
