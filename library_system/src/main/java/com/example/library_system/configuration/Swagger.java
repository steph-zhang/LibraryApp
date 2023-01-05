package com.example.library_system.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableKnife4j
@Configuration
@EnableOpenApi
public class Swagger {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(true)
                .groupName("ZRJ")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.library_system.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @SuppressWarnings("all")
    public ApiInfo apiInfo(){
        return new ApiInfo(
                "xiaoduan's api",
                "redis project",
                "v1.0",
                "xiaoduan0110@163.com", //开发者团队的邮箱
                "ZRJ",
                "Apache 2.0",  //许可证
                "http://www.apache.org/licenses/LICENSE-2.0" //许可证链接
        );
    }
}
