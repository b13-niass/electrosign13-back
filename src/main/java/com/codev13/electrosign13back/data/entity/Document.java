package com.codev13.electrosign13back.data.entity;

import com.codev13.electrosign13back.enums.EtatDocument;
import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "documents")
@SQLDelete(sql = "UPDATE documents SET etat = 'ARCHIVE' WHERE id=?")
public class Document extends AbstractEntity implements GenericEntity<Document> {
    private String nom;
    private String contenu;

    @Enumerated(EnumType.STRING)
    private EtatDocument etatDocument;

    @OneToMany(mappedBy = "document")
    private List<SignatureDocument> signatures;
    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;

    @Override
    public void update(Document source) {
        this.nom = source.getNom();
        this.contenu = source.getContenu();
        this.etatDocument = source.getEtatDocument();
        this.signatures = source.getSignatures();
        this.demande = source.getDemande();
    }

    @Override
    public Document createNewInstance() {
        Document document = new Document();
        document.update(this);
        return document;
    }
}

