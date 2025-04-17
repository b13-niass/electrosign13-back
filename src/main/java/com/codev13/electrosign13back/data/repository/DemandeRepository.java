package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.enums.StatusDemande;
import com.core.communs.core.GenericRepository;

import java.util.List;

public interface DemandeRepository extends GenericRepository<Demande> {
    List<Demande> findByStatus(StatusDemande status);
}
