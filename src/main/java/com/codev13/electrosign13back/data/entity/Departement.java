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
@Table(name = "departements")
public class Departement extends AbstractEntity implements GenericEntity<Departement>{
    @Column(unique = true, nullable = false)
    private String libelle;
    @Column(unique = true, nullable = false)
    private String acronyme;

    @ManyToMany
    @JoinTable(
            name = "departement_filiale",
            joinColumns = @JoinColumn(name = "departement_id"),
            inverseJoinColumns = @JoinColumn(name = "filiale_id")
    )
    @JsonIgnore
    private List<Filiale> filiales;

    @OneToMany(mappedBy = "departement")
    private List<Fonction> fonctions;

    @Override
    public void update(Departement source) {
        this.libelle = source.getLibelle();
        this.acronyme = source.getAcronyme();
        this.filiales = source.getFiliales();
    }

    @Override
    public Departement createNewInstance() {
        Departement departement = new Departement();
        departement.update(this);
        return departement;
    }
}
