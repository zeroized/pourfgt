package cn.edu.shu.pourfgt.security;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PostgraduateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Integer grade = (Integer) session.getAttribute("grade");
        if (grade == null) {
            response.sendRedirect("/login");
            return false;
        } else {
            if (1 == grade) {
                return true;
            } else {
                if (grade == 0) {
                    response.sendRedirect("/student/course/list");
                    return false;
                } else {
                    response.sendRedirect("/teacher/course/list");
                    return false;
                }
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
