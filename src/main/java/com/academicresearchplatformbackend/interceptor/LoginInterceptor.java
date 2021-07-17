package com.academicresearchplatformbackend.interceptor;




import com.academicresearchplatformbackend.dao.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] requiredAuthPages = new String[]{
                "menus"
        };
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath + "/");
        String page = uri;

//        return true;

        if (beginWith(page, requiredAuthPages)) {
            //
            // User user = (User) session.getAttribute("user");
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("login");
                return false;
            }
        }
        return true;
    }

    private boolean beginWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages
        ) {
            if (StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }

        return result;
    }
}
