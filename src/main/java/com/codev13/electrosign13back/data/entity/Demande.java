package com.codev13.electrosign13back.data.entity;
import com.codev13.electrosign13back.enums.StatusDemande;
import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "demandes")
public class Demande extends AbstractEntity implements GenericEntity<Demande>  {
    private String titre;
    private String description;
    private Date dateLimite;
    private int nombreApprobation;
    private int nombreSignature;
    private int currentApprobation;
    private int currentSignature;

    @Enumerated(EnumType.STRING)
    private StatusDemande status;

    @OneToMany(mappedBy = "demande")
    private List<DemandeSignature> demandeSignatures;
    @OneToMany(mappedBy = "demande")
    private List<Document> documents;

    @Override
    public void update(Demande source) {
        this.titre = source.getTitre();
        this.description = source.getDescription();
        this.dateLimite = source.getDateLimite();
        this.nombreApprobation = source.getNombreApprobation();
        this.nombreSignature = source.getNombreSignature();
        this.status = source.getStatus();
        this.currentApprobation = source.getCurrentApprobation();
        this.currentSignature = source.getCurrentSignature();
        this.demandeSignatures = source.getDemandeSignatures();
        this.documents = source.getDocuments();
    }

    @Override
    public Demande createNewInstance() {
        Demande demande = new Demande();
        demande.update(this);
        return demande;
    }
}
