package com.cucumber.market.api.dto.region;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Getter
    @Setter
    public static class level2 {
        private String level1;
    }

    @Getter
    @Setter
    public static class level3 {
        private String level1;

        private String level2;
    }

    @Getter
    @Setter
    public static class level4 {
        private String level1;

        private String level2;

        private String level3;
    }

    @Getter
    @Setter
    public static class level5 {
        private String level1;

        private String level2;

        private String level3;

        private String level4;
    }

    @Getter
    @Setter
    public static class id {

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
