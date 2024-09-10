package com.cucumber.market.api.service.history;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class HistoryService {

    @Getter
    @Setter
    public static class sales {
        @NotNull
        private Integer memberId;

        private Integer itemStatus;
    }
}
