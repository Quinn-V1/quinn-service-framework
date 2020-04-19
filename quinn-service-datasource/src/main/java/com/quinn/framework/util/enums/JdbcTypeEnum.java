package com.quinn.framework.util.enums;

import com.quinn.util.base.util.CollectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * JDBC类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public enum JdbcTypeEnum {

    // 字符串
    VARCHAR(Types.VARCHAR, String.class),

    // 4字节(32位)整数（-2^32 ~ 2^32 - 1）
    INTEGER(Types.INTEGER, Integer.class, Integer.TYPE),

    // 8字节(64位)整数（-2^64 ~ 2^64 - 1）
    BIGINT(Types.BIGINT, Long.class, Long.TYPE),

    // 2字节(16位)整数（-2^16 ~ 2^16 - 1）
    SMALLINT(Types.SMALLINT, Short.class, Short.TYPE),

    // 日期时间
    TIMESTAMP(Types.TIMESTAMP, LocalDateTime.class, Timestamp.class, java.util.Date.class),

    // 时间
    DATE(Types.DATE, LocalDate.class, Date.class, java.util.Date.class),

    // 指定长度
    DECIMAL(Types.DECIMAL, BigDecimal.class),

    // 双精度数字
    DOUBLE(Types.DOUBLE, Double.class, Double.TYPE),

    // 1位整数（0、1）
    BIT(Types.BIT, Boolean.class, Boolean.TYPE),

    // 1字(8位)节整数（-2^8 ~ 2^8 - 1）
    TINYINT(Types.TINYINT, Byte.class, Byte.TYPE),

    // 长字符串
    NVARCHAR(-9, String.class),

    // 时间
    TIME(Types.TIME, LocalTime.class, Time.class),

    FLOAT(Types.FLOAT, Float.class, Float.TYPE),

    NUMERIC(Types.NUMERIC, BigDecimal.class),

    REAL(Types.REAL, BigDecimal.class),

    CHAR(Types.CHAR, Character.class, Character.TYPE),

    LONG_VARCHAR(Types.LONGVARCHAR),

    NCHAR(-15),

    LONG_NVARCHAR(-16),

    BINARY(Types.BINARY),

    VARBINARY(Types.VARBINARY),

    LONG_VARBINARY(Types.LONGVARBINARY),

    BOOLEAN(Types.BOOLEAN),

    BLOB(Types.BLOB, Blob.class),

    CLOB(Types.CLOB, Clob.class),

    NCLOB(2011, NClob.class),

    NULL(Types.NULL),

    OTHER(Types.OTHER),

    CURSOR(-10),

    UNDEFINED(Integer.MIN_VALUE + 1000),

    ROW_ID(-8),

    SQL_XML(2009),

    ;

    /**
     * JDBC类型
     */
    public final int code;

    /**
     * JDBC类型
     */
    public final Class[] javaClazz;

    /**
     * 全参构造器
     *
     * @param code          类型编码
     * @param javaClazz     Java类型
     */
    JdbcTypeEnum(int code, Class... javaClazz) {
        this.code = code;
        this.javaClazz = javaClazz;
    }

    /**
     * 获取JDBC类型名称
     *
     * @param code      JDBC编码
     * @return          JDBC名称
     */
    public static String getJdbcSqlTypeName(int code) {
        for (JdbcTypeEnum type : values()) {
            if (type.code == code) {
                return type.name();
            }
        }
        return UNDEFINED.name();
    }

    /**
     * 获取JDBC类型
     *
     * @param jdbcSqlTypeName   JDBC名称
     * @return                  JDBC编码
     */
    public static int getJdbcSqlTypeByName(String jdbcSqlTypeName) {
        for (JdbcTypeEnum type : values()) {
            if (type.name().equals(jdbcSqlTypeName)) {
                return type.code;
            }
        }
        return UNDEFINED.code;
    }

    /**
     * 获取JDBC类型
     *
     * @param javaClazz   JDBC名称
     * @return            DBC编码
     */
    public static JdbcTypeEnum getJdbcSqlTypeByName(Class javaClazz) {
        for (JdbcTypeEnum type : values()) {
            if (CollectionUtil.elementInArray(javaClazz, type.javaClazz)) {
                return type;
            }
        }
        return UNDEFINED;
    }

}