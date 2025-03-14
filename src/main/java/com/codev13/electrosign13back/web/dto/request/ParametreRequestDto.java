package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.enums.StrategieSecurite;
import com.codev13.electrosign13back.enums.TypeStockage;
import jakarta.validation.constraints.NotNull;

public record ParametreRequestDto(
        @NotNull
        String nomEntreprise,
        String logoEntreprise,
        StrategieSecurite strategieSecurite,
        TypeStockage typeStockage
) {
}
