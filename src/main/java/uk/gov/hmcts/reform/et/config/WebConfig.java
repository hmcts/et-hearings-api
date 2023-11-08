package uk.gov.hmcts.reform.et.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.hmcts.reform.et.config.interceptors.RequestInterceptor;

@Component
public class WebConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    public WebConfig(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
            .excludePathPatterns(
                "/health",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v2/**",
                "/favicon.ico",
                "/health",
                "/mappings",
                "/info",
                "/metrics",
                "/metrics/**",
                "/v3/**",
                "/"
            );
    }
}
