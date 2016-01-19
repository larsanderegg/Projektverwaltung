package ch.lan.teko.repository;
import ch.lan.teko.model.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = Phase.class)
public interface PhaseRepository extends JpaSpecificationExecutor<Phase>, JpaRepository<Phase, Long> {
}
