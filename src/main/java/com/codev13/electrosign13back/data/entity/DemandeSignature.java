package com.codev13.electrosign13back.data.entity;

import com.codev13.electrosign13back.enums.DemandeSignatureActions;
import com.core.communs.core.GenericEntity;

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
@Table(name = "demande_signature")
public class DemandeSignature extends AbstractEntity implements GenericEntity<DemandeSignature> {
    @Enumerated(EnumType.STRING)
    private DemandeSignatureActions action;
    private int ordre;
    private int detenant;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Override
    public void update(DemandeSignature source) {
        this.action = source.getAction();
        this.ordre = source.getOrdre();
        this.demande = source.getDemande();
        this.sender = source.getSender();
        this.receiver = source.getReceiver();
        this.detenant = source.getDetenant();
    }

    @Override
    public DemandeSignature createNewInstance() {
        DemandeSignature demandeSignature = new DemandeSignature();
        demandeSignature.update(this);
        return demandeSignature;
    }
}

