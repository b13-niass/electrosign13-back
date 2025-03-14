package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.StrategieSecurite;
import com.codev13.electrosign13back.enums.TypeStockage;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametreResponseDto {
    private Long id;
    private String nomEntreprise;
    private String logoEntreprise;
    private StrategieSecurite strategieSecurite;
    private TypeStockage typeStockage;
}
