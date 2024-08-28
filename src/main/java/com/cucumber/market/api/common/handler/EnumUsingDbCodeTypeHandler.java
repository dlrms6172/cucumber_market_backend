package com.cucumber.market.api.common.handler;

import com.cucumber.market.api.service.item.ItemStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ItemStatus.class)
public class EnumUsingDbCodeTypeHandler <E extends Enum<E> & EnumUsingDbCode> extends BaseTypeHandler<E> {

    private Class<E> type;
    private final E[] enumConstants;

    public EnumUsingDbCodeTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enumConstants = type.getEnumConstants();
        if (this.enumConstants == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getDbCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int dbCode = rs.getInt(columnName);
        return rs.wasNull() ? null : getEnum(dbCode);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int dbCode = rs.getInt(columnIndex);
        return rs.wasNull() ? null : getEnum(dbCode);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int dbCode = cs.getInt(columnIndex);
        return cs.wasNull() ? null : getEnum(dbCode);
    }

    private E getEnum(int dbCode) {
        for (E enm : enumConstants) {
            if (dbCode == enm.getDbCode()) {
                return enm;
            }
        }
        throw new IllegalArgumentException("Cannot convert " + dbCode + " to " + type.getSimpleName());
    }
}
