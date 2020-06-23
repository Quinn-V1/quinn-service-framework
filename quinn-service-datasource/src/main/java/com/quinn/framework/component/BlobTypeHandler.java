package com.quinn.framework.component;

import com.quinn.util.base.StreamUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.sql.*;

/**
 * Blob类型字段处理器
 *
 * @author Qunhua.Liao
 * @since 2020-06-23
 */
public class BlobTypeHandler extends BaseTypeHandler<String> {

    /**
     * <p>Title：setNonNullParameter</p>
     * <p>Description：insert或者update时前处理blob字段 </p>
     *
     * @param ps        PreparedStatement
     * @param i         参数序号
     * @param parameter 参数值
     * @param jdbcType  类型
     * @throws SQLException SQL异常
     * @see BaseTypeHandler#setNonNullParameter(PreparedStatement, int, Object, JdbcType)
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE);
            return;
        }

        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(StringUtil.getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET));
            ps.setBinaryStream(i, bis);
        } finally {
            StreamUtil.closeQuietly(bis);
        }
    }

    /**
     * <p>Title：getNullableResult</p>
     * <p>Description： 查询成功后处理blob字段</p>
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return 字符串结果
     * @throws SQLException SQL异常
     * @see BaseTypeHandler#getNullableResult(ResultSet, String)
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        if (null != blob) {
            byte[] returnValue = blob.getBytes(1, (int) blob.length());
            return StringUtil.forBytes(returnValue);
        } else {
            return null;
        }
    }

    /**
     * <p>Title：getNullableResult</p>
     * <p>Description： 查询成功后处理blob字段 </p>
     *
     * @param rs          结果集
     * @param columnIndex 列序号
     * @return 字符串结果
     * @throws SQLException SQL异常
     * @see BaseTypeHandler#getNullableResult(ResultSet, int)
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        if (null != blob) {
            byte[] returnValue = blob.getBytes(1, (int) blob.length());
            return StringUtil.forBytes(returnValue);
        } else {
            return null;
        }
    }

    /**
     * <p>Title：getNullableResult</p>
     * <p>Description：  查询成功后处理blob字段</p>
     *
     * @param cs          结果集
     * @param columnIndex 列序号
     * @return 字符传结果
     * @throws SQLException SQL异常
     * @see BaseTypeHandler#getNullableResult(CallableStatement, int)
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        if (null != blob) {
            byte[] returnValue = blob.getBytes(1, (int) blob.length());
            return StringUtil.forBytes(returnValue);
        } else {
            return null;
        }
    }

}