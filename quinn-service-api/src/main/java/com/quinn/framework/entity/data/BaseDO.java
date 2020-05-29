package com.quinn.framework.entity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.enums.DataStatusEnum;
import com.quinn.util.constant.enums.DbOperateTypeEnum;
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
     * 缓存主键分割符
     */
    public static final String CACHE_KEY_DELIMITER = BaseDTO.CACHE_KEY_DELIMITER;

    /**
     * 数据主键分割符
     */
    public static final String DATA_KEY_DELIMITER = BaseDTO.DATA_KEY_DELIMITER;

    /**
     * 属性主键分割符
     */
    public static final String PROPERTY_DELIMITER = BaseDTO.PROPERTY_DELIMITER;

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
     * 数据状态
     */
    @ApiModelProperty("数据状态")
    private Integer dataStatus;

    /**
     * 顶层组织
     */
    @ApiModelProperty("顶层组织")
    private String rootOrg;

    /**
     * 数据库操作
     */
    @JsonIgnore
    private DbOperateTypeEnum dbOperateType;

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
     * @return 本身
     */
    public <T> T prepareForInsert(String userKey, String orgKey) {
        this.rootOrg = orgKey;
        this.dataVersion = NumberConstant.INT_ONE;
        this.insertUser = this.updateUser = userKey;
        this.insertDateTime = this.updateDateTime = LocalDateTime.now();
        this.dbOperateType = DbOperateTypeEnum.INSERT;

        if (this.dataStatus == null) {
            this.dataStatus = DataStatusEnum.NORMAL.code;
        }
        return (T) this;
    }

    /**
     * 更新前准备
     *
     * @param userKey 用户编码
     * @param allFlag 用户编码
     * @return 本身
     */
    public <T> T prepareForUpdate(String userKey, boolean allFlag) {
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
        this.dbOperateType = allFlag ?  DbOperateTypeEnum.UPDATE_ALL : DbOperateTypeEnum.UPDATE_NON_EMPTY;
        return (T) this;
    }

    /**
     * 删除前准备
     *
     * @param userKey 用户编码
     * @param hardFlag 用户编码
     * @return 本身
     */
    public <T> T prepareForDelete(String userKey, boolean hardFlag) {
        this.prepareForUpdate(userKey, false);
        if (dataVersion == null || dataVersion.intValue() == 0) {
            this.dataVersion = -1;
            return (T) this;
        }
        if (dataVersion.intValue() < 0) {
            return (T) this;
        }

        this.dataVersion = -dataVersion - 1;
        this.dbOperateType = hardFlag ? DbOperateTypeEnum.DELETE_HARD : DbOperateTypeEnum.DELETE_SOFT;
        return (T) this;
    }

    /**
     * 恢复前准备
     *
     * @param userKey 用户编码
     * @param hardFlag 用户编码
     * @return 本身
     */
    public <T> T prepareForRecover(String userKey, boolean hardFlag) {
        this.prepareForUpdate(userKey, false);
        if (dataVersion == null || dataVersion.intValue() == 0) {
            this.dataVersion = 1;
            return (T) this;
        }

        if (dataVersion.intValue() > 0) {
            return (T) this;
        }

        this.dataVersion = -dataVersion + 1;
        this.dbOperateType = hardFlag ?  DbOperateTypeEnum.RECOVERY_HARD : DbOperateTypeEnum.RECOVERY_SOFT;
        return (T) this;
    }

    @Override
    public String toString() {
        return cacheKey();
    }

    public String cacheKey() {
        return this.getClass().getSimpleName() + DATA_KEY_DELIMITER + dataKey();
    }
}
