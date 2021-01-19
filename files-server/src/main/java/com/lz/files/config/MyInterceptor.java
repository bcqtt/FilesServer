package com.lz.files.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    /**
     * preHandle在执行Controller之前执行，返回true，则继续执行Contorller
     * 返回false则请求中断。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o)
            throws Exception {
        //只有返回true才会继续向下执行，返回false取消当前请求 
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    /**
     * postHandle是在请求执行完，但渲染ModelAndView返回之前执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
            ModelAndView modelAndView) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-type,Accept");
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, " +
                        "Last-Modified, Cache-Control, Expires");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        StringBuilder sb = new StringBuilder(1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        sb.append("\n-----------------------").append(date).append("-------------------------------------\n");
        sb.append("URI   : ").append(request.getRequestURI()).append("\n");
        sb.append("耗时  : ").append(executeTime).append("ms").append("\n");
        sb.append("-------------------------------------------------------------------------------");
        log.info(sb.toString());
    }

    /**
     * afterCompletion是在整个请求执行完毕后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) throws Exception {
    }

}