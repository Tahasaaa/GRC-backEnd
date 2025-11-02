package org.example.grc.Repository;

import org.example.grc.Entities.RecEtat;
import org.example.grc.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByUserId(Long userId);

    List<Reclamation> findByEtat_RecEtat(RecEtat recEtat);

    List<Reclamation> findByRecRef(String reference);




}
