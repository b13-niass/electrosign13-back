package com.codev13.electrosign13back.data.entity;

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
@Table(name = "signature_documents")
public class SignatureDocument extends AbstractEntity implements GenericEntity<SignatureDocument> {
    private String documentHash;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public void update(SignatureDocument source) {
        this.documentHash = source.getDocumentHash();
        this.document = source.getDocument();
    }

    @Override
    public SignatureDocument createNewInstance() {
        SignatureDocument signatureDocument = new SignatureDocument();
        signatureDocument.update(this);
        return signatureDocument;
    }
}
