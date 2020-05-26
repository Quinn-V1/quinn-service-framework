package com.quinn.framework.configuration;

import com.quinn.framework.api.file.StorageService;
import com.quinn.framework.service.impl.MiniIoStorageServiceImpl;
import com.quinn.util.constant.OrderedConstant;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mini Io 配置
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
@Configuration
@AutoConfigureOrder(OrderedConstant.LOWER_V2)
public class MinIioConfiguration {

    @Bean("storageService")
    public StorageService iinIioStorageService() {
        MiniIoStorageServiceImpl storageService = new MiniIoStorageServiceImpl();
        return storageService;
    }

}
