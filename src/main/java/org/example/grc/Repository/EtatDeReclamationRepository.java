package org.example.grc.Repository;
import org.example.grc.Entities.EtatDeReclamation;
import org.example.grc.Entities.RecEtat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EtatDeReclamationRepository extends JpaRepository<EtatDeReclamation, Long> {
}
