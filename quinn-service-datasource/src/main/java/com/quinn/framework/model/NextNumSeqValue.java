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
public class NextNumSeqValue {

    /**
     * 序列名
     */
    private String seqName;

    /**
     * 序列数
     */
    private int seqNum;

    /**
     * 开始值
     */
    private long seqValue;

    /**
     * 步长
     */
    private int seqStep;

    /**
     * 获取所有序列
     *
     * @return 序列值
     */
    public long[] seqValues() {
        long[] result = new long[seqNum];
        for (int i = 0; i < seqNum; i++) {
            result[i] = seqValue + seqStep * i;
        }
        return result;
    }
}
