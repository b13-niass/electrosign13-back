//package com.codev13.electrosign13back.config;
//
//import com.core.communs.config.BaseSwaggerConfig;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class SwaggerConfig extends BaseSwaggerConfig {
//    @Autowired
//    public SwaggerConfig(@Value("${spring.application.version}") String version) {
//        this.version = version;
//    }
//    @Bean
//    public GroupedOpenApi groupeOpenApi() {
//        return GroupedOpenApi.builder()
//                .group("elecro-sign-13")
//                .packagesToScan("com.codev13.electrosign13back.web.controller")
//                .packagesToExclude("com.codev13.electrosign13back.data.entity")
//                .build();
//    }
//}