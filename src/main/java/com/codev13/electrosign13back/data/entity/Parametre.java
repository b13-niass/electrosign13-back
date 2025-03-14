package com.codev13.electrosign13back.data.entity;

import com.codev13.electrosign13back.enums.StrategieSecurite;
import com.codev13.electrosign13back.enums.TypeStockage;
import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "parametres")
public class Parametre extends AbstractEntity implements GenericEntity<Parametre>{
    private String nomEntreprise;
    private String logoEntreprise;

    @Enumerated(EnumType.STRING)
    private StrategieSecurite strategieSecurite;

    @Enumerated(EnumType.STRING)
    private TypeStockage typeStockage;

    @Override
    public void update(Parametre source) {
        this.nomEntreprise = source.getNomEntreprise();
        this.logoEntreprise = source.getLogoEntreprise();
        this.strategieSecurite = source.getStrategieSecurite();
        this.typeStockage = source.getTypeStockage();
    }

    @Override
    public Parametre createNewInstance() {
        Parametre parametre = new Parametre();
        parametre.update(this);
        return parametre;
    }
}

