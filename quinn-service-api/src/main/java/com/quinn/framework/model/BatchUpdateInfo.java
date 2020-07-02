package com.quinn.framework.model;

import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.constant.enums.MessageLevelEnum;
import com.quinn.util.base.enums.NotifyEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量更新操作数据封装对象
 *
 * @author Qunhua.Liao
 * @since 2020-04-20
 */
@Getter
@Setter
public class BatchUpdateInfo<T extends BaseDO> {

    public BatchUpdateInfo() {
    }

    public BatchUpdateInfo(List<T> oldValues, List<T> newList) {
        this.oldValues = oldValues;
        this.newList = newList;
    }

    /**
     * 旧列表值
     */
    @ApiModelProperty(value = "旧前页面")
    private List<T> oldValues;

    /**
     * 新列表值
     */
    @ApiModelProperty(value = "新列表值")
    private List<T> newList;

    /**
     * 标记增、删、改、查
     *
     * @param allFlag   全量标识
     * @param hardFlag  硬删除标识
     * @return          是否成功标记
     */
    public BaseResult<List<T>> flag(boolean allFlag, boolean hardFlag) {
        if (CollectionUtil.isEmpty(oldValues) && CollectionUtil.isEmpty(newList)) {
            return BaseResult.success(null).ofLevel(MessageLevelEnum.WARN)
                    .buildMessage(NotifyEnum.NOTHING_HAPPENED.key(), 0, 0)
                    .result();
        }

        String userKey = SessionUtil.getUserKey();
        String orgKey = SessionUtil.getOrgKey();

        if (CollectionUtil.isEmpty(oldValues)) {
            for (T t : newList) {
                t.prepareForInsert(userKey, orgKey);
            }
            return BaseResult.success(newList);
        } else if (CollectionUtil.isEmpty(newList)) {
            for (T t : oldValues) {
                t.prepareForDelete(userKey, hardFlag);
            }
            return BaseResult.success(oldValues);
        } else {
            Map<String, T> oldKeys = new HashMap<>();
            for (T t : oldValues) {
                oldKeys.put(t.dataKey(), t);
            }

            for (T t : newList) {
                String dataKey = t.dataKey();
                if (oldKeys.containsKey(dataKey)) {
                    t.setId(oldKeys.get(dataKey).getId());
                    t.prepareForUpdate(userKey, allFlag);
                    oldKeys.remove(dataKey);
                } else {
                    t.setId(null);
                    t.prepareForInsert(userKey, orgKey);
                }
                oldValues.remove(dataKey);
            }

            for (Map.Entry<String, T> entry : oldKeys.entrySet()) {
                newList.add(entry.getValue().prepareForDelete(userKey, hardFlag));
            }
            return BaseResult.success(newList);
        }
    }

}
