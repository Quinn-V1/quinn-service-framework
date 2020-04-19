package com.quinn.framework.util;

import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.FilePathStrategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 文件路径策略工具
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public class FilePathStrategyUtil {

    private FilePathStrategyUtil() {
    }

    private static final Map<String, FilePathStrategy> STRATEGY_MAP = new HashMap<>();

    static {
        ServiceLoader<FilePathStrategy> filePathStrategies = ServiceLoader.load(FilePathStrategy.class);
        Iterator<FilePathStrategy> iterator = filePathStrategies.iterator();
        while (iterator.hasNext()) {
            FilePathStrategy strategy = iterator.next();
            STRATEGY_MAP.put(strategy.name(), strategy);
        }
    }

    /**
     * 装饰路径
     *
     * @param fileInfoAdapter 文件信息
     */
    public static void decoratePath(FileInfoAdapter fileInfoAdapter) {
        String[] pathStrategies = fileInfoAdapter.getPathStrategies();
        if (pathStrategies != null) {
            for (String pathStrategy : pathStrategies) {
                FilePathStrategy strategy = STRATEGY_MAP.get(pathStrategy);
                if (strategy != null) {
                    strategy.decoratePath(fileInfoAdapter);
                }
            }
        }
    }

}
