package com.cucumber.market.api.common.handler;

/**
 * Enum as JavaType <-> Integer as JDBCType (SQL 처리 시 사용할 컬럼 값)
 */
public interface EnumUsingDbCode {
    int getDbCode();
}
