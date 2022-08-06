package cn.wl.reggie.filter;


//检查用户是否已经完成登录


import cn.wl.reggie.common.BaseContext;
import cn.wl.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        /*
        过滤器具体的处理逻辑如下：
        1、获取本次请求的URI
        2、判断本次请求是否需要处理
        3、如果不需要处理，则直接放行
        4、判断登录状态，如果已登录，则直接放行
        5、如果未登录则返回未登录结果
         */


        //1.获取本次请求的URI
        String requestURL = request.getRequestURI();
        log.info("拦截到： {}",requestURL);
            //定义不需要处理的请求路径
        String[] urls= new String[]{
               "/employee/login" ,
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };


        //2判断本次请求是否需要处理
        boolean check = check(urls, requestURL);
        //3如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不用处理",requestURL);

            filterChain.doFilter(request,response);
            return;
        }
        //4-1判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));


           Long empId =(Long) request.getSession().getAttribute("employee");

//            long id = Thread.currentThread().getId();
            BaseContext.setCurrentId(empId);


            filterChain.doFilter(request,response);
            return;
        }
        //4-2
        if(request.getSession().getAttribute("user")!=null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));


            Long userId =(Long) request.getSession().getAttribute("user");

//            long id = Thread.currentThread().getId();
            BaseContext.setCurrentId(userId);


            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录则返回未登录结果,通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("拦截到：{}",request.getRequestURL());
        return;

    }
    //路径请求，检查本次请求是否需要放行
    public  boolean check( String[] urls,String requestURL){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURL);
            if(match){
                return  true;
            }

        }
        return false;


    }

}
