package com.cucumber.market.api.service.item;

import com.cucumber.market.api.common.handler.EnumUsingDbCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemStatus implements EnumUsingDbCode {
    ON_SALE(1),
    RESERVED(2),
    CLOSED(3);

    private final int itemStatusCode;

    @Override
    public int getDbCode() {
        return itemStatusCode;
    }
}
