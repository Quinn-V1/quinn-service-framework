package com.quinn.framework.entity.dto;

import com.quinn.framework.util.enums.AppendWayEnum;
import com.quinn.framework.util.enums.JoinTypeEnum;
import com.quinn.framework.util.enums.SqlCondWrapperEnum;
import com.quinn.util.constant.CharConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 关联表
 *
 * @author Qunhua.Liao
 * @since 2020-09-28
 */
@Getter
@Setter
public class JoinTable {

    public JoinTable(BaseDTO dto, String tableAlias, JoinTypeEnum joinType) {
        this.dto = dto;
        this.tableAlias = tableAlias;
        this.joinType = joinType;
    }

    /**
     * 关联条件对虾薄情
     */
    private BaseDTO dto;

    /**
     * 关联类型
     */
    private JoinTypeEnum joinType;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 关联字段
     */
    private List<OnField> onFields = new ArrayList<>();

    /**
     * 添加关联条件
     *
     * @param lAlias 左侧表别名
     * @param lField 左侧字段
     * @return 本身
     */
    JoinTable on(String lAlias, String lField) {
        on(lAlias, lField, BaseDTO.PROP_NAME_OF_ID, SqlCondWrapperEnum.EQUAL);
        return this;
    }

    /**
     * 添加关联条件
     *
     * @param lAlias 左侧表别名
     * @param lField 左侧字段
     * @param rField 右侧字段
     * @return 本身
     */
    JoinTable on(String lAlias, String lField, String rField) {
        on(lAlias, lField, rField, SqlCondWrapperEnum.EQUAL);
        return this;
    }

    /**
     * 添加关联条件
     *
     * @param lAlias  左侧表别名
     * @param lField  左侧字段
     * @param rField  右侧字段
     * @param wrapper 条件包装
     * @return 本身
     */
    JoinTable on(String lAlias, String lField, String rField, SqlCondWrapperEnum wrapper) {
        onFields.add(new OnField(lAlias, lField, rField, wrapper));
        return this;
    }

    /**
     * JOIN 关联字段
     *
     * @author Qunhua.Liao
     * @since 2020-09-28
     */
    class OnField {

        public OnField(String lAlias, String lField, String rField, SqlCondWrapperEnum wrapper) {
            this.lAlias = lAlias;
            this.lField = lField;
            this.rField = rField;
            this.wrapper = wrapper;
        }

        private String lAlias;

        private String lField;

        private String rField;

        private SqlCondWrapperEnum wrapper;

        @SneakyThrows
        void appendTo(Appendable query, String appendWay) {
            query.append(CharConstant.BLANK).append(appendWay).append(CharConstant.BLANK)
                    .append(lAlias).append(CharConstant.DOT)
                    .append(dto.columnOfProp(lField));
            wrapper.wrap(query, rField, tableAlias);
        }
    }

    /**
     * 拼接到字符串上
     */
    @SneakyThrows
    public void appendTo(Appendable query) {
        query.append(CharConstant.BLANK).append(joinType.code)
                .append(CharConstant.BLANK).append(dto.tableName())
                .append(CharConstant.BLANK).append(tableAlias).append(CharConstant.BLANK)
        ;

        Iterator<OnField> iterator = onFields.iterator();
        iterator.next().appendTo(query, AppendWayEnum.ON.name());

        while (iterator.hasNext()) {
            iterator.next().appendTo(query, AppendWayEnum.AND.name());
        }
    }

}
