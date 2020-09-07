package com.quinn.framework.component.intercept;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.api.methodflag.GetFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.entity.data.IdGenerateAbleDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.service.CacheAbleService;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.MessageLevelEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 缓存读过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
@Component
public class CacheAbleGetEntityServiceInterceptor implements EntityServiceInterceptor {

    @Value("${com.quinn-service.cache.entity-cache-expire-time:1800}")
    private long defaultExpireTime;

    @Resource
    private CacheAbleService cacheAbleService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{CacheAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{GetFlag.class};
    }

    @Override
    public <T> void before(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        if (t instanceof CacheAble) {
            if (t instanceof BaseDTO && !((BaseDTO) t).getUseCache()) {
                return;
            }

            CacheAble cacheAble = (CacheAble) t;
            String cacheKey = cacheAble.cacheKey();
            if (cacheKey.contains(StringConstant.NULL_OF_STRING)) {
                cacheKey = cacheKeyOfId(cacheAble);
                if (cacheKey == null) {
                    return;
                }
                cacheKey = (String) cacheAbleService.get(cacheKey, String.class).getData();
                if (cacheKey == null) {
                    return;
                }
            }

            BaseResult cacheRes = cacheAbleService.get(cacheKey, cacheAble.getEntityClass());
            if (cacheRes.isSuccess()) {
                result.setData(cacheRes.getData());
                result.ofLevel(MessageLevelEnum.WARN);
            }
        }
    }

    @Override
    public <T> void after(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        Object object = result.getData();
        if (object instanceof CacheAble) {
            CacheAble cacheAble = (CacheAble) object;
            String cacheKey = cacheAble.cacheKey();

            if (StringUtil.isNotEmpty(cacheKey)) {
                cacheAbleService.set(cacheKey, cacheAble, defaultExpireTime);
                cacheAbleService.set(cacheKeyOfId(cacheAble), cacheKey);
            }
        }
    }

    /**
     * ID主键缓存
     *
     * @param cacheAble 可缓存对象
     * @return 键
     */
    static String cacheKeyOfId(CacheAble cacheAble) {
        if (cacheAble.getId() == null) {
            return null;
        } else {
            return IdGenerateAbleDO.CACHE_KEY_ID_TO_DATA_KEY + cacheAble.getEntityClass().getSimpleName()
                    + StringConstant.CHAR_COLON + cacheAble.getId();
        }
    }

}
