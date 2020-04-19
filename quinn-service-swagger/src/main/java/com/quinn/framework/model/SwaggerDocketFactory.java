package com.quinn.framework.model;

import com.google.common.base.Predicates;
import com.quinn.util.base.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger文档分组配置
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@Setter
@Getter
public class SwaggerDocketFactory implements FactoryBean<Docket> {

    /**
     * 组名
     */
    private String groupName;

    /**
     * 显示路径样式
     */
    private String pathRegex;

    /**
     * 根路径
     */
    private String basePackage;

    /**
     * API文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * URL
     */
    private String termsOfServiceUrl;

    /**
     * 版本号
     */
    private String version;

    public SwaggerDocketFactory() {
        super();
    }

    public SwaggerDocketFactory(
            String groupName, String pathRegex, String basePackage,
            String title, String description, String termsOfServiceUrl, String version
    ) {
        setGroupName(groupName);
        setPathRegex(pathRegex);
        setBasePackage(basePackage);
        setTitle(title);
        setDescription(description);
        setTermsOfServiceUrl(termsOfServiceUrl);
        setVersion(version);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .version(version)
                .build();
    }

    @Nullable
    @Override
    public Docket getObject() {
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(StringUtil.isEmpty(pathRegex) ? PathSelectors.any() : PathSelectors.regex(pathRegex))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(StringUtil.isEmpty(basePackage) ? RequestHandlerSelectors.any() : RequestHandlerSelectors.basePackage(basePackage))
                .build();

        return swaggerSpringMvcPlugin;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return Docket.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
