package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.Role;
import com.core.communs.core.GenericRepository;

import java.util.Optional;

public interface RoleRepository extends GenericRepository<Role> {
    void deleteByLibelle(String libelle);

    Optional<Role> findRoleByLibelle(String libelle);
    boolean existsByLibelle(String libelle);
}
