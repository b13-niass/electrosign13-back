//package com.codev13.electrosign13back.config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173") // Specify frontend origin
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true); // Allow credentials (cookies, authorization headers)
//    }
//
//}