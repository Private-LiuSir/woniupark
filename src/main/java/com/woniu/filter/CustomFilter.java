package com.woniu.filter;

import com.woniu.component.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        System.out.println("开始验证。。。。。。。");
        //判断是否登录
        if(isLoginAttempt(request,response)){
            try {
                executeLogin(request,response);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        //如果没带token，意味着匿名访问，直接放行，由shiro的鉴权操作进行处理
        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        //转为httprequest
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        //获取请求头中的token
        String token = httpRequest.getHeader("token");
        //返回一个boolean值
        return token!=null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        //转为httprequest
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        //获取请求头中
        String token = httpRequest.getHeader("token");
        //创建自定义token
        JwtToken jwtToken = new JwtToken(token);
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //全局异常处理器处理  不用关心是否登录成功
        System.out.println("subject:"+subject);
        subject.login(jwtToken);
        //返回 true 有异常往上抛
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //转为httprequest
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        HttpServletResponse httpResponse=(HttpServletResponse)response;
        //处理跨域
        httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
        //判断是否为复杂请求的预链接
        if(httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
