package com.cucumber.market.api.dto.region;

import lombok.Getter;
import lombok.Setter;

public class RegionDto {

    @Getter
    @Setter
    public static class regionAdd{
        private String siDo;

        private String siGunGu;

        private String eupMyeonDongGu;

        private String eupMyeonLiDong;

        private String li;

        private float latiTude;

        private float longiTude;
    }
}
