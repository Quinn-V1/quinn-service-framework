package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.constant.ConfigConstant;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 模块扫描收集器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class ModuleScanConfigurationCollector implements ConfigInfoCollector {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(ModuleScanConfigurationCollector.class);

    @Override
    public int getPriority() {
        return NumberConstant.INT_HUNDRED * NumberConstant.INT_THREE;
    }

    @SneakyThrows
    @Override
    public void collect(Properties priorityProperties) {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources(ConfigConstant.MODULE_DEFINITION_RESOURCES);
        if (CollectionUtil.isEmpty(resources)) {
            return;
        }

        StringBuilder queryBeanPackages = new StringBuilder();
        StringBuilder queryMapperPackages = new StringBuilder();
        StringBuilder queryNamespacePackages = new StringBuilder();
        StringBuilder swaggerDocGroups = new StringBuilder();
        StringBuilder configurationClassNameIgnoredQuery = new StringBuilder();

        Set<String> beanPackages = new HashSet<>();
        Set<String> mapperPackages = new HashSet<>();
        Set<String> namespacePackages = new HashSet<>();
        Set<String> moduleGroupSet = new HashSet<>();
        Set<String> configurationClassNameIgnoredSet = new HashSet<>();

        for (Resource resource : resources) {
            Properties properties = new Properties();
            InputStream is = null;
            try {
                is = resource.getInputStream();
                if (is != null) {
                    properties.load(is);
                } else {
                    continue;
                }

                String beanPackage = properties.getProperty(ConfigConstant.PACKAGE_NAME_MODULES_BEAN);
                String mapperPackage = properties.getProperty(ConfigConstant.PACKAGE_NAME_MODULES_MAPPER);
                String namespacePackage = properties.getProperty(ConfigConstant.NAMESPACE_NAME_MODULES);
                String configurationClassNameIgnored = properties.getProperty(ConfigConstant.CONFIGURATION_CLASS_NAME_IGNORED);

                String moduleGroups = properties.getProperty(ConfigConstant.GROUPS_NAME_MODULES, StringConstant.STRING_EMPTY);
                String restfulDocApiUrlPattern = properties.getProperty(ConfigConstant.RESTFUL_DOC_API_URL_PATTERN, StringConstant.STRING_EMPTY);
                String moduleName = StringConstant.STRING_EMPTY;

                if (StringUtil.isNotEmpty(configurationClassNameIgnored) &&
                        !configurationClassNameIgnoredSet.contains(configurationClassNameIgnored)) {
                    configurationClassNameIgnoredQuery.append(CharConstant.COMMA).append(configurationClassNameIgnored);
                    configurationClassNameIgnoredSet.add(configurationClassNameIgnored);
                }

                if (StringUtil.isNotEmpty(namespacePackage) &&
                        !namespacePackages.contains(namespacePackage)) {
                    queryNamespacePackages.append(CharConstant.COMMA).append(namespacePackage);

                    moduleName = namespacePackage.replace(ConfigConstant.PACKAGE_PATH_BASE, StringConstant.STRING_EMPTY)
                            .replace(CharConstant.DOT, CharConstant.SLASH);
                    namespacePackages.add(namespacePackage);
                }

                if (StringUtil.isNotEmpty(namespacePackage)) {
                    if (StringUtil.isNotEmpty(moduleGroups)) {
                        String[] moduleGroupArray = StringUtil.split(moduleGroups, StringConstant.CHAR_COMMA);
                        for (String moduleGroup : moduleGroupArray) {
                            String group = namespacePackage + moduleGroup;

                            if (moduleGroupSet.contains(group)) {
                                continue;
                            }

                            swaggerDocGroups.append(CharConstant.SEMICOLON)
                                    .append(moduleName)
                                    .append(CharConstant.SLASH)
                                    .append(moduleGroup)
                                    .append(CharConstant.COLON)
                                    .append(restfulDocApiUrlPattern)
                                    .append(CharConstant.COLON)
                                    .append(namespacePackage)
                                    .append(moduleGroup)
                            ;
                            moduleGroupSet.add(group);
                        }
                    } else if (!moduleGroupSet.contains(namespacePackage)) {
                        swaggerDocGroups.append(CharConstant.SEMICOLON)
                                .append(moduleName)
                                .append(CharConstant.SLASH)
                                .append(CharConstant.COLON)
                                .append(restfulDocApiUrlPattern)
                                .append(CharConstant.COLON)
                                .append(namespacePackage)
                        ;
                        moduleGroupSet.add(namespacePackage);
                    }
                }

                if (StringUtil.isNotEmpty(beanPackage) &&
                        !beanPackages.contains(beanPackage)) {
                    queryBeanPackages.append(CharConstant.COMMA).append(beanPackage);

                    beanPackages.add(beanPackage);
                }

                if (StringUtil.isNotEmpty(mapperPackage)
                        && !mapperPackages.contains(mapperPackage)) {
                    queryMapperPackages.append(CharConstant.COMMA).append(mapperPackage);
                    mapperPackages.add(mapperPackage);
                }

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        if (configurationClassNameIgnoredQuery.length() > 0) {
            String oldIgnore = priorityProperties.getProperty(ConfigConstant.CONFIGURATION_CLASS_NAME_IGNORED);
            if (StringUtil.isEmpty(oldIgnore)) {
                configurationClassNameIgnoredQuery.deleteCharAt(0);
            } else {
                configurationClassNameIgnoredQuery.insert(0, oldIgnore);
            }

            priorityProperties.setProperty(ConfigConstant.CONFIGURATION_CLASS_NAME_IGNORED,
                    configurationClassNameIgnoredQuery.toString());
        }

        priorityProperties.setProperty(ConfigConstant.PACKAGE_NAME_MODULES_BEAN, queryBeanPackages.toString());
        priorityProperties.setProperty(ConfigConstant.PACKAGE_NAME_MODULES_MAPPER, queryMapperPackages.toString());
        priorityProperties.setProperty(ConfigConstant.NAMESPACE_NAME_MODULES, queryNamespacePackages.toString());

        if (swaggerDocGroups.length() > 0) {
            swaggerDocGroups.deleteCharAt(0);
            priorityProperties.setProperty(ConfigConstant.RESTFUL_DOC_API_GROUPS, swaggerDocGroups.toString());
        }
    }

}
