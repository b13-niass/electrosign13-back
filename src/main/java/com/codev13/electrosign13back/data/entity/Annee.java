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
@Table(name = "annees")
public class Annee extends AbstractEntity implements GenericEntity<Annee>{
    @Column(unique = true, nullable = false)
    private String libelle;

    @ManyToMany
    @JoinTable(
            name = "annee_filiale",
            joinColumns = @JoinColumn(name = "annee_id"),
            inverseJoinColumns = @JoinColumn(name = "filiale_id")
    )
    @JsonIgnore
    private List<Filiale> filiales;

    @Override
    public void update(Annee source) {
        this.libelle = source.getLibelle();
        this.filiales = source.getFiliales();
    }

    @Override
    public Annee createNewInstance() {
        Annee annee = new Annee();
        annee.update(this);
        return annee;
    }
}

