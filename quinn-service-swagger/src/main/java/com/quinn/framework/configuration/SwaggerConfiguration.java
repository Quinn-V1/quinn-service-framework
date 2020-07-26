package com.quinn.framework.configuration;

import com.quinn.framework.model.SwaggerDocketFactory;
import com.quinn.util.constant.ConfigConstant;
import com.quinn.util.base.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger-ui文档配置
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@Configuration
@EnableSwagger2
@ConditionalOnExpression("'${com.quinn-service.core.http.restful.doc:true}'=='true'")
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    /**
     * 文档分组
     */
    private String docGroups;

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


    @Override
    public void setEnvironment(Environment environment) {
        this.docGroups = environment.getProperty(ConfigConstant.RESTFUL_DOC_API_GROUPS, "");
        this.title = environment.getProperty(ConfigConstant.RESTFUL_DOC_API_TITLES, "");
        this.description = environment.getProperty(ConfigConstant.RESTFUL_DOC_API_DESCRIPTIONS, "");
        this.termsOfServiceUrl = environment.getProperty(ConfigConstant.RESTFUL_DOC_API_TERMS_OF_URL, "");
        this.version = environment.getProperty(ConfigConstant.RESTFUL_DOC_API_VERSIONS, "");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        this.docGroups = StringUtil.isNotEmpty(this.docGroups) ? this.docGroups : "All:/.*:";
        if (StringUtil.isNotEmpty(this.docGroups)) {
            String[] parts = this.docGroups.split(";");
            for (String part : parts) {
                if (!StringUtil.isNotEmpty(part)) {
                    continue;
                }

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                String[] groupInfo = part.split(":");

                String groupName = groupInfo[0];
                String pathRegex;
                String basePackage = "";
                if (groupInfo.length > 2) {
                    basePackage = groupInfo[2];
                    pathRegex = groupInfo[1];
                } else if (groupInfo.length == 2) {
                    pathRegex = groupInfo[1];
                } else {
                    // 根据group名称生成一个组, 自动包含这个组名下所有的接口
                    pathRegex = groupName.endsWith("/") ? (groupName + ".*") : (groupName + "/.*");
                }
                beanDefinition.setBeanClass(SwaggerDocketFactory.class);
                ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addGenericArgumentValue(groupName);
                constructorArgumentValues.addGenericArgumentValue(pathRegex);
                constructorArgumentValues.addGenericArgumentValue(basePackage);
                constructorArgumentValues.addGenericArgumentValue(title);
                constructorArgumentValues.addGenericArgumentValue(description);
                constructorArgumentValues.addGenericArgumentValue(termsOfServiceUrl);
                constructorArgumentValues.addGenericArgumentValue(version);

                beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
                beanDefinitionRegistry.registerBeanDefinition("swaggerSpringFoxDocket" + (groupName.hashCode()), beanDefinition);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
