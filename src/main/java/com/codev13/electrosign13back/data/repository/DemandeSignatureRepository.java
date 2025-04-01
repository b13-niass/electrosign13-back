package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.DemandeSignature;
import com.codev13.electrosign13back.enums.DemandeSignatureActions;
import com.core.communs.core.GenericRepository;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DemandeSignatureRepository extends GenericRepository<DemandeSignature> {
    List<DemandeSignature> findBySenderId(Long senderId);

    List<DemandeSignature> findByReceiverId(Long receiverId);

    @Query("SELECT ds FROM DemandeSignature ds WHERE ds.demande.id = :demandeId ORDER BY ds.ordre ASC")
    List<DemandeSignature> findByDemandeIdOrderByOrdre(@Param("demandeId") Long demandeId);

    @Query("SELECT ds FROM DemandeSignature ds WHERE ds.demande.id = :demandeId AND ds.detenant = 1")
    DemandeSignature findCurrentSigner(@Param("demandeId") Long demandeId);

    List<DemandeSignature> findByDemandeIdAndAction(Long demandeId, DemandeSignatureActions action);

    List<DemandeSignature> findByDemandeIdAndActionOrderByOrdre(Long demandeId, DemandeSignatureActions action);

    Optional<DemandeSignature> findByDemandeIdAndReceiverIdAndAction(Long demandeId, Long receiverId, DemandeSignatureActions action);

    Optional<DemandeSignature> findByDemandeIdAndReceiverId(Long demandeId, Long receiverId);
    boolean existsBySenderIdOrReceiverIdAndDemandeId(Long senderId, Long receiverId, Long demandeId);
}
