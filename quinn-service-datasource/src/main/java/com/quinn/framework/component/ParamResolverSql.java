package com.quinn.framework.component;

import com.quinn.framework.api.ParamResolver;
import com.quinn.util.constant.enums.ParamTypeEnum;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.handler.DefaultPlaceholderHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sql参数解析器
 *
 * @author Qunhua.Liao
 * @since 2020-02-11
 */
@Service("paramResolverSQL")
public class ParamResolverSql implements ParamResolver {

    /**
     * 从参数中获取参数类型的键
     */
    public static final String PARAM_KEY_SQL = "paramSql";

    /**
     * 从参数中获取参数类型的键
     */
    public static final String PARAM_KEY_SQL_SINGLE_RESULT = "singleResult";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BaseResult<Map<String, Object>> resolve(Map<String, Object> messageParam) {
        String sql = (String) messageParam.remove(PARAM_KEY_SQL);
        if (StringUtils.isEmpty(sql)) {
            return BaseResult.fail("参数SQL文【paramSql】没有指定");
        }

        List<Object> params = new ArrayList<>();
        sql = DefaultPlaceholderHandler.defaultInstance().parseSqlParamMapToList(sql, params, messageParam);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, params.toArray(new Object[params.size()]));
        if (CollectionUtils.isEmpty(maps)) {
            return BaseResult.fail("数据库没有找到合适参数");
        }

        if (BaseConverter.staticConvert(messageParam.get(PARAM_KEY_SQL_SINGLE_RESULT), Boolean.class)) {
            messageParam.putAll(maps.get(0));
            return BaseResult.success(messageParam);
        }

        messageParam.put(ParamTypeEnum.PARAM_KEY_SQL_RESULT, maps);
        return BaseResult.success(messageParam);
    }

}
