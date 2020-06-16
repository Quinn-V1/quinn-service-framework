package com.quinn.framework.bpm7.model;

import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmInstSupplier;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.util.base.model.ClassComparator;

import java.util.*;

/**
 * Act信息转通用BPM信息工厂
 *
 * @author Qunhua.Liao
 * @since @2020-06-15
 */
public class ActToBpmInfoFactory {

    private static Map<Class, EventToBpmTaskDecorator> decoratorMap = new LinkedHashMap<>();

    private static BpmInstSupplier bpmInstSupplier;

    static {
        ServiceLoader<EventToBpmTaskDecorator> decorators = ServiceLoader.load(EventToBpmTaskDecorator.class);
        Iterator<EventToBpmTaskDecorator> decoratorIterator = decorators.iterator();
        List<EventToBpmTaskDecorator> list = new ArrayList<>();
        while (decoratorIterator.hasNext()) {
            list.add(decoratorIterator.next());
        }
        Collections.sort(list, new ClassComparator());
        for (EventToBpmTaskDecorator decorator : decorators) {
            addDecoratorMap(decorator);
        }
    }

    public static BpmTaskInfo toTaskInfo(Object t) {
        if (t == null) {
            return null;
        }

        BpmTaskInfo bpmTaskInfo = bpmInstSupplier.newBpmTaskInfo();
        EventToBpmTaskDecorator decorator = decoratorMap.get(t.getClass());
        if (decorator != null) {
            decorator.decorate(bpmTaskInfo, t);
            return bpmTaskInfo;
        } else {
            for (Map.Entry<Class, EventToBpmTaskDecorator> entry : decoratorMap.entrySet()) {
                if (entry.getKey().isAssignableFrom(t.getClass())) {
                    entry.getValue().decorate(bpmTaskInfo, t);
                }
            }
        }
        return bpmTaskInfo;
    }

    /**
     * 添加装饰器
     *
     * @param decorator 装饰器
     */
    public static void addDecoratorMap(EventToBpmTaskDecorator decorator) {
        decoratorMap.put(decorator.getDivClass(), decorator);
    }

    /**
     * 设置实例对象提供者
     *
     * @param bpmInstSupplier 实例对象提供者
     */
    public static void setBpmInstSupplier(BpmInstSupplier bpmInstSupplier) {
        ActToBpmInfoFactory.bpmInstSupplier = bpmInstSupplier;
    }
}
