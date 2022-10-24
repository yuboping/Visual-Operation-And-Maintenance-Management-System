package com.asiainfo.lcims.omc.filter;

/**
 * @Author: YuChao
 * @Date: 2019/8/16 10:59
 */
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RefererFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void init(FilterConfig config) {
    }


    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 链接来源地址
        String referer = request.getHeader("Referer");

//        if (referer == null || !referer.contains(request.getServerName())) {
////          request.getRequestDispatcher("/error.jsp").forward(request, response);
//          response.sendRedirect("/");
//
//        } else {
            chain.doFilter(request, response);
//        }

    }


    @Override
    public void destroy() {
    }

}
