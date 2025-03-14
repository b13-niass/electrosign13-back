package com.codev13.electrosign13back.data.entity;

import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "filiales")
public class Filiale extends AbstractEntity implements GenericEntity<Filiale> {
   @Column(unique = true, nullable = false)
    private String libelle;
   @Column(unique = true, nullable = false)
    private String acronyme;

    @ManyToMany(mappedBy = "filiales")
    @JsonIgnore
    private List<Departement> departements;

    @ManyToMany(mappedBy = "filiales")
    @JsonIgnore
    private List<Annee> annees;

    @Override
    public void update(Filiale source) {
        this.libelle = source.getLibelle();
        this.acronyme = source.getAcronyme();
        this.departements = source.getDepartements();
        this.annees = source.getAnnees();
    }

    @Override
    public Filiale createNewInstance() {
        Filiale filiale = new Filiale();
        filiale.update(this);
        return filiale;
    }
}

