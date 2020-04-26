package com.quinn.framework.configuration;

import com.quinn.framework.api.file.FileHandler;
import com.quinn.framework.api.file.StorageService;
import com.quinn.framework.component.file.DemoFileHandler;
import com.quinn.framework.component.LocalStorageServiceImpl;
import com.quinn.framework.service.IdGenerateAbleService;
import com.quinn.framework.service.impl.DefaultIdGenerateAbleServiceImpl;
import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.framework.service.AuditAbleService;
import com.quinn.framework.service.CacheAbleService;
import com.quinn.framework.service.ParameterAbleService;
import com.quinn.framework.service.impl.DefaultAuditAbleServiceImpl;
import com.quinn.framework.service.impl.DefaultCacheAbleServiceImpl;
import com.quinn.framework.service.impl.DefaultParameterAbleServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 基础配置类：其中的Bean在生产环境中需要根据需要换掉
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE - 1000)
public class BaseBizConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = {"applicationInfo"})
    public ApplicationInfo applicationInfo() {
        return ApplicationInfo.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"cacheAbleService"})
    public CacheAbleService cacheAbleService() {
        return new DefaultCacheAbleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"parameterAbleService"})
    public ParameterAbleService parameterAbleService() {
        return new DefaultParameterAbleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"auditAbleService"})
    public AuditAbleService auditAbleService() {
        return new DefaultAuditAbleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"idGenerateAbleService"})
    public IdGenerateAbleService idGenerateAbleService() {
        return new DefaultIdGenerateAbleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"storageService"})
    public StorageService storageService() {
        return new LocalStorageServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(value = {FileHandler.class})
    public FileHandler fileHandler(StorageService storageService) {
        return new DemoFileHandler(storageService);
    }

    @Bean
    @ConditionalOnMissingBean(value = {RestTemplate.class})
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
