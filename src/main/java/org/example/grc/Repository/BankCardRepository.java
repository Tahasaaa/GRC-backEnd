package org.example.grc.Repository;

import org.example.grc.Entities.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {
    List<BankCard> findByUser_Email(String email);
}
