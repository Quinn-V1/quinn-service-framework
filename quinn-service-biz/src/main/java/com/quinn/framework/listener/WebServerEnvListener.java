package com.quinn.framework.listener;

import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.IpUtil;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 基础配置类：其中的Bean在生产环境中需要根据需要换掉
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
@Component
public class WebServerEnvListener implements ApplicationListener<WebServerInitializedEvent> {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(WebServerEnvListener.class);

    @Value("${server.address:}")
    private String serverAddress;

    @Value("${server.port:}")
    private Integer serverPort;

    @Value("${server.servlet.context-path:}")
    private String servletContextPath;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Resource
    private ApplicationInfo applicationInfo;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        String host = null;
        if (StringUtils.isEmpty(serverAddress)) {
            List<String> ips = IpUtil.getLocalIPList();
            if (ips.size() == 1) {
                host = ips.get(0);
            } else {
                if (ips.size() == 0) {
                    LOGGER.error("没有配置IP地址");
                } else if (ips.size() > 1) {
                    LOGGER.error("操作系统指定了多个IP地址，请配置 server.address");
                }
                System.exit(-1);
            }
        } else {
            int index = serverAddress.indexOf(":");
            if (index > 1) {
                host = serverAddress.substring(0, index);
            } else {
                host = serverAddress;
            }
        }

        applicationInfo.setHost(host);
        applicationInfo.setPort(event.getWebServer().getPort());
        if (!StringUtils.isEmpty(applicationName)) {
            applicationInfo.setApplicationName(applicationName);
        } else {
            if (!StringUtils.isEmpty(servletContextPath)) {
                applicationInfo.setApplicationName(servletContextPath.replace('/', '-'));
            }
        }

        applicationInfo.generateKey();
    }

}
