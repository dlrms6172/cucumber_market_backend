package com.cucumber.market.api.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class RegionDto {

    @Getter
    @Setter
    public static class RegionAddDto {
        private String siDo;

        private String siGunGu;

        private String eupMyeonDongGu;

        private String eupMyeonLiDong;

        private String li;

        private float latiTude;

        private float longiTude;
    }

    @Getter
    @Setter
    public static class Level2Dto {
        private String level1;
    }

    @Getter
    @Setter
    public static class Level3Dto {
        private String level1;

        private String level2;
    }

    @Getter
    @Setter
    public static class Level4Dto {
        private String level1;

        private String level2;

        private String level3;
    }

    @Getter
    @Setter
    public static class Level5Dto {
        private String level1;

        private String level2;

        private String level3;

        private String level4;
    }

    @Getter
    @Setter
    public static class IdDto {

        @NotBlank
        private String level1;

        @NotBlank
        private String level2;

        @NotBlank
        private String level3;

        private String level4;

        private String level5;
    }
}
