package com.cucumber.market.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public class ItemSeller {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Info {
        private Integer memberId;
        private String name;
        private BigDecimal mannersTemperature;
        private String profileImageUrl;
    }

    @NoArgsConstructor
    @Getter
    public static class Region {
        private String siDo;
        private String siGunGu;
        private String eupMyeonDongGu;
        private String eupMyeonLiDong;
        private String li;
    }
}
