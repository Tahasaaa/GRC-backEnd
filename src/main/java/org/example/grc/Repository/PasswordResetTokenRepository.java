package org.example.grc.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.example.grc.Entities.PasswordResetToken;
import org.example.grc.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    @Transactional
    void deleteByUser(UserEntity user);

}

