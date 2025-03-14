package com.codev13.electrosign13back.data.entity;

import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@Table(name = "fonctions")
public class Fonction extends AbstractEntity implements GenericEntity<Fonction> {

    @Column(unique = true, nullable = false)
    private String libelle;
    @Column(unique = true, nullable = false)
    private String acronyme;

    @OneToMany(mappedBy = "fonction")
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @Override
    public void update(Fonction source) {
        this.libelle = source.getLibelle();
        this.acronyme = source.getAcronyme();
        this.users = source.getUsers();
    }

    @Override
    public Fonction createNewInstance() {
        Fonction fonction = new Fonction();
        fonction.update(this);
        return fonction;
    }
}
