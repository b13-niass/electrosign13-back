package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.enums.TypeDocument;
import com.core.communs.core.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends GenericRepository<Document> {
    Optional<Document> findFirstByDemandeIdAndTypeAndActiveTrue(Long demandeId, TypeDocument type);
}
