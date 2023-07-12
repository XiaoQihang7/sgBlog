package com.sg.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    public static final String TAG_1 = "评论相关";
    public static final String TAG_2 = "文章相关";
    public static final String TAG_3 = "前台登入相关";
    public static final String TAG_4 = "分类相关";
    public static final String TAG_5 = "友链相关";
    public static final String TAG_6 = "用户相关";
    public static final String TAG_7 = "上传相关";

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        //扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage("com.sg.controller")).build()
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                //tag标签与描述绑定
                .tags(new Tag(TAG_1, "评论相关接口"))
                .tags(new Tag(TAG_2, "文章相关接口."))
                .tags(new Tag(TAG_3, "登入相关接口."))
                .tags(new Tag(TAG_4, "分类相关接口."))
                .tags(new Tag(TAG_5, "友链相关接口."))
                .tags(new Tag(TAG_6, "用户相关接口."))
                .tags(new Tag(TAG_7, "上传相关接口."));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("航の专业团队", "http://www.xxx.com", "2529201093@qq.com");
        return new ApiInfoBuilder()
                .title("My Blog API")
                .description("欢迎参考航的博客接口文档")
                .contact(contact)
                .version("1.0.0").build();
    }
}
