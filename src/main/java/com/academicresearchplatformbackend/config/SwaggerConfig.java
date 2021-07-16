package com.academicresearchplatformbackend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.academicresearchplatformbackend"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
//                        标题
                        .title("高校科研创新服务平台")
//                        详细信息
                        .description("小学期项目")
//                        版本
                        .version("1.0")
//                        联系人信息
//                        .contact()
//                        许可
//                        .license("The Apache License")
//                        许可证连接
//                        .licenseUrl("http://www.baidu.com")
                        .build());
    }
}

