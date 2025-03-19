package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.Fonction;
import com.core.communs.core.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FonctionRepository extends GenericRepository<Fonction> {
    Optional<Fonction> findFonctionById(Long id);
    boolean existsById(Long id);
}
