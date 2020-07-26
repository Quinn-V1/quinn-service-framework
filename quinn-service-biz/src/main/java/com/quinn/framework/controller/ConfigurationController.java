package com.quinn.framework.controller;

import com.quinn.framework.component.BaseConfigInfoReWriter;
import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.util.constant.ConfigConstant;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 框架配置接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/framework/configuration/*")
@Api(tags = {"0ZZ080框架：配置查询"})
public class ConfigurationController extends AbstractController {

    /**
     * 应用实例信息
     */
    @Resource
    private ApplicationInfo applicationInfo;

    @GetMapping(value = "property")
    @ApiOperation(value = "查询应用的配置参数")
    public BaseResult getApplicationRuntimeConfigurationJson() {
        Properties runtimeConfigurations = new Properties();
        runtimeConfigurations.putAll(applicationInfo.getApplicationProperties());

        BaseConfigInfoReWriter.decryptProperties(runtimeConfigurations);
        return BaseResult.success(runtimeConfigurations);
    }

    @GetMapping(value = "namespaces")
    @ApiOperation(value = "查询应用配置项命名空间")
    public BaseResult getApplicationConfigurationNamespaces() {
        List<String> ret = new ArrayList<>();
        String namespace = applicationInfo.getApplicationProperties()
                .getProperty(ConfigConstant.NAMESPACE_NAME_MODULES);
        if (StringUtil.isNotEmpty(namespace)) {
            String[] namespaces = namespace.split(",");
            for (String n : namespaces) {
                if (StringUtils.hasText(n)) {
                    ret.add(n.trim());
                }
            }
        }
        return BaseResult.success(ret);
    }

    @GetMapping(value = "metadata")
    @ApiOperation("查询配置元数据")
    public BaseResult getApplicationConfigurationMetadata() {
        return BaseResult.success(applicationInfo.getConfigMetadataMap());
    }

}
