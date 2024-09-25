package com.cucumber.market.api.dto.history;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class HistoryDto {
    @Getter
    @Setter
    public static class sales {
        private Integer memberId;

        private Integer itemStatusId;
    }

    @Getter
    @Setter
    public static class purchase {
        private Integer memberId;
    }

    @Getter
    @Setter
    public static class interests {
        private Integer memberId;
    }

    @Getter
    @Setter
    public static class itemStatus {
        private Integer memberId;
    }
}
