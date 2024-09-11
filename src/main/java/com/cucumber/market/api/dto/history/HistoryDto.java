package com.cucumber.market.api.dto.history;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class HistoryDto {
    @Getter
    @Setter
    public static class sales {
        @NotNull
        private Integer memberId;

        private Integer itemStatusId;
    }

    @Getter
    @Setter
    public static class purchase {
        @NotNull
        private Integer memberId;
    }

    @Getter
    @Setter
    public static class interests {
        @NotNull
        private Integer memberId;
    }

    @Getter
    @Setter
    public static class itemStatus {
        @NotNull
        private Integer memberId;
    }
}
