package com.quinn.framework.component;

import com.quinn.framework.api.ParamResolver;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.handler.PlaceholderHandler;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.CommonParamName;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public BaseResult<Map<String, Object>> resolve(Map<String, Object> messageParam) {
        String sql = (String) messageParam.remove(CommonParamName.PARAM_KEY_SQL);
        if (StringUtils.isEmpty(sql)) {
            // FIXME
            return BaseResult.fail("参数SQL文【paramSql】没有指定");
        }

        List<Object> params = new ArrayList<>();
        sql = PlaceholderHandler.defaultInstance().parseSqlParamMapToList(sql, params, messageParam);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, params.toArray(new Object[params.size()]));
        if (CollectionUtils.isEmpty(maps)) {
            // FIXME
            return BaseResult.fail("数据库没有找到合适参数");
        }

        if (BaseConverter.staticConvert(messageParam.get(CommonParamName.PARAM_KEY_SQL_SINGLE_RESULT), Boolean.class)) {
            messageParam.putAll(maps.get(0));
            return BaseResult.success(messageParam);
        }

        messageParam.put(CommonParamName.PARAM_KEY_RUNTIME_PARAM, maps);
        return BaseResult.success(messageParam);
    }

}
