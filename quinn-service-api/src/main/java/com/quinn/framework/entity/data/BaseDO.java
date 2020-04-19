package com.quinn.framework.entity.data;

import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.util.constant.NumberConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public abstract class BaseDO implements Serializable, IdGenerateAble {

    /**
     * 系统主键
     */
    @ApiModelProperty("系统主键")
    private Long id;

    /**
     * 插入用户
     */
    @ApiModelProperty("插入用户")
    private String insertUser;

    /**
     * 更新用户
     */
    @ApiModelProperty("更新用户")
    private String updateUser;

    /**
     * 插入时间
     */
    @ApiModelProperty("插入时间")
    private LocalDateTime insertDateTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateDateTime;

    /**
     * 数据版本
     */
    @ApiModelProperty("数据版本")
    private Integer dataVersion;

    /**
     * 归档标识
     */
    @ApiModelProperty("归档标识")
    private Integer archiveFlag;

    /**
     * 顶层组织
     */
    @ApiModelProperty("顶层组织")
    private String rootOrg;

    /**
     * 获取缓存Key
     *
     * @return 缓存Key
     */
    public String dataKey() {
        return String.valueOf(id);
    }

    @Override
    public String seqName() {
        return this.getClass().getName();
    }

    /**
     * 新增前准备
     *
     * @param orgKey  组织编码
     * @param userKey 用户编码
     */
    public void prepareForInsert(String userKey, String orgKey) {
        this.rootOrg = orgKey;
        this.archiveFlag = NumberConstant.INT_ZERO;
        this.dataVersion = NumberConstant.INT_ONE;
        this.insertUser = this.updateUser = userKey;
        this.insertDateTime = this.updateDateTime = LocalDateTime.now();
    }

    /**
     * 更新前准备
     *
     * @param userKey 用户编码
     */
    public void prepareForUpdate(String userKey) {
        this.updateUser = userKey;
        this.updateDateTime = LocalDateTime.now();
        if (dataVersion == null) {
            this.dataVersion = NumberConstant.INT_TWO;
        } else {
            if (dataVersion >= 0) {
                this.dataVersion = dataVersion + 1;
            } else {
                dataVersion = dataVersion - 1;
            }
        }
    }

    /**
     * 删除前准备
     *
     * @param userKey 用户编码
     */
    public boolean prepareForDelete(String userKey) {
        this.prepareForUpdate(userKey);
        if (dataVersion == null || dataVersion.intValue() == 0) {
            this.dataVersion = -1;
            return true;
        }
        if (dataVersion.intValue() < 0) {
            return false;
        }

        this.dataVersion = -dataVersion - 1;
        return true;
    }

    /**
     * 恢复前准备
     *
     * @param userKey 用户编码
     */
    public boolean prepareForRecover(String userKey) {
        this.prepareForUpdate(userKey);
        if (dataVersion == null || dataVersion.intValue() == 0) {
            this.dataVersion = 1;
            return true;
        }

        if (dataVersion.intValue() > 0) {
            return false;
        }

        this.dataVersion = -dataVersion + 1;
        return true;
    }

    @Override
    public String toString() {
        return cacheKey();
    }

    public String cacheKey() {
        return this.getClass().getSimpleName() + ":" + dataKey();
    }
}
