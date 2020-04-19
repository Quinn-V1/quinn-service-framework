package com.quinn.framework.configuration;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.quinn.framework.api.file.StorageService;
import com.quinn.framework.service.impl.FastDfsStorageServiceImpl;
import com.quinn.util.constant.OrderedConstant;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.*;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * FastDfs文件上传下载
 *
 * @author Qunhua.Liao
 * @since 2020-04-17
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@AutoConfigureOrder(OrderedConstant.LOWER_V1)
public class FastDfsConfiguration {

    // 引入 com.github.tobato 相关Bean

    @Primary
    @Bean("storageService")
    public StorageService storageService() {
        return new FastDfsStorageServiceImpl();
    }

}
