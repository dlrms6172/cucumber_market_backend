package com.cucumber.market.api.service.review;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewSort {
    MY_REVIEW(true),
    YOUR_REVIEW(false);

    private final boolean isFromMe;

    public boolean getBoolean() {
        return isFromMe;
    }
}
