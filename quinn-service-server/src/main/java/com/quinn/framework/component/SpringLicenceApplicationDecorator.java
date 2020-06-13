package com.quinn.framework.component;

import com.quinn.framework.api.SpringApplicationDecorator;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.factory.LicenceClassLoader;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.LicenceInfo;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.LicenceExceptionType;
import com.quinn.util.constant.enums.SystemExitTypeEnum;
import com.quinn.util.licence.model.ApplicationInfo;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * 容器授权许可装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-04-18
 */
public class SpringLicenceApplicationDecorator implements SpringApplicationDecorator {

    @Override
    @SneakyThrows
    public void decorate(SpringApplication springApplication, Properties properties) {
        String licenceDir = properties.getProperty(ConfigConstant.PROP_KEY_OF_LICENCE_PATH,
                ConfigConstant.DEFAULT_LICENCE_PATH);

        URL resource = null;
        File file = new File(licenceDir);
        if (!file.exists()) {
            if (licenceDir.startsWith(ConfigConstant.CONFIG_KEY_PREFIX_CLASSPATH) ||
                    licenceDir.startsWith(ConfigConstant.CONFIG_KEY_PREFIX_CLASSPATH_ALL)) {

                String licenceDirTemp = licenceDir.substring(licenceDir.indexOf(StringConstant.CHAR_COLON) + 1);
                file = new File(licenceDirTemp);
                if (file.exists()) {
                    resource = file.toURI().toURL();
                }
            }

            if (resource == null) {
                PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources(licenceDir);
                if (!CollectionUtil.isEmpty(resources)) {
                    resource = resources[0].getURL();
                }
            }
        } else {
            resource = file.toURI().toURL();
        }

        if (resource == null) {
            Integer errCode = LicenceExceptionType.UNAUTHORIZED.code + SystemExitTypeEnum.LICENCE_ERROR.code;
            System.err.println(CommonMessageEnum.LICENCE_EXCEPTION.name() + "[" + errCode + "]");
            System.exit(errCode);
        }

        BaseResult<LicenceInfo> init = LicenceClassLoader.init(resource,
                SpringLicenceApplicationDecorator.class.getClassLoader());

        if (!init.isSuccess()) {
            Integer errCode = LicenceExceptionType.FILE_DESTROYED.code + SystemExitTypeEnum.LICENCE_ERROR.code;
            System.err.println(CommonMessageEnum.LICENCE_EXCEPTION.name() + "[" + errCode + "]");
            System.exit(errCode);
        }

        ApplicationInfo.init(init.getData());
    }

}
