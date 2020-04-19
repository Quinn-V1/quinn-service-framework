package com.quinn.framework.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 以后若干个序列值
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
@Setter
@Getter
public class NextNumSeqCondition {

    /**
     * 序列名
     */
    private String seqName;

    /**
     * 个数
     */
    private int num;

}
