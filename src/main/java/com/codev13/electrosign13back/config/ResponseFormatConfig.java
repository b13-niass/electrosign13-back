package com.codev13.electrosign13back.config;

import com.core.communs.web.filter.ResponseFormattingFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseFormatConfig {
    @Bean
    public Filter responseFormattingFilter() {
        return new ResponseFormattingFilter();
    }
}