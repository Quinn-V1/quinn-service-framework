package com.quinn.framework.api.entityflag;

/**
 * 需要ID表标识
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
public interface IdGenerateAble {

    /**
     * 设置ID
     *
     * @param id    ID
     */
    void setId(Long id);

    /**
     * 生成ID的类型
     *
     * @return
     */
    String seqName();

}
