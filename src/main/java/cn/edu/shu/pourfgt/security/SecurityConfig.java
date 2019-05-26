package cn.edu.shu.pourfgt.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TeacherInterceptor())
                .addPathPatterns("/teacher/**");
        registry.addInterceptor(new PostgraduateInterceptor())
                .addPathPatterns("/student/postgraduate/**");
        registry.addInterceptor(new UndergraduateInterceptor())
                .addPathPatterns("/student/course/**", "student/graduation/**");
    }
}
