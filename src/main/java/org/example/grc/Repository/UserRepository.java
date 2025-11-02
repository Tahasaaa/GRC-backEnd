package org.example.grc.Repository;


import org.example.grc.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//ay haja reliee bel base de donnees
@Repository
public interface UserRepository extends JpaRepository <UserEntity, Long> {

    boolean existsByUsername(String username);
    UserEntity findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);
}
