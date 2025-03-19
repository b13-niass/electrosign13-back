package com.codev13.electrosign13back.data.repository;

import com.codev13.electrosign13back.data.entity.User;
import com.core.communs.core.GenericRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {
    @EntityGraph(attributePaths = {"fonction"})
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
    boolean existsByCni(String cni);
    @EntityGraph(attributePaths = {"fonction"})
    Optional<User> findByEmail(String email);
}
