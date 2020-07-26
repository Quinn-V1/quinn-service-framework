package com.quinn.framework.component;

import com.quinn.framework.api.CustomApplicationListener;
import com.quinn.framework.component.serializer.JsonApplicationSerializer;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StreamUtil;
import com.quinn.util.constant.ConfigConstant;
import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.util.licence.model.ConfigMetadata;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 元数据收集器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class MetaDataConfigInfoCollector implements CustomApplicationListener {

    @Override
    @SneakyThrows
    public void applicationStarted(ApplicationContext applicationContext) {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources(ConfigConstant.MODULE_DEFINITION_METADATA);
        if (CollectionUtil.isEmpty(resources)) {
            return;
        }

        JsonApplicationSerializer jsonSerializer = new JsonApplicationSerializer();
        Map<String, ConfigMetadata> configMetadataMap = new HashMap<>(resources.length);
        for (Resource resource : resources) {
            try {
                InputStream is = resource.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                StreamUtil.copy(is, os);

                ConfigMetadata configMetadata = jsonSerializer.deserialize(os.toByteArray(), ConfigMetadata.class);
                configMetadataMap.put(configMetadata.getGroup(), configMetadata);

                is.close();
                os.close();
            } catch (Exception e) {
                // TODO warn
            }
        }
        ApplicationInfo.getInstance().setConfigMetadataMap(configMetadataMap);

    }
}
