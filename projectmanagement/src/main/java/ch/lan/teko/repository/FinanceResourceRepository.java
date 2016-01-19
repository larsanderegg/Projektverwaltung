package ch.lan.teko.repository;
import ch.lan.teko.model.FinanceResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = FinanceResource.class)
public interface FinanceResourceRepository extends JpaRepository<FinanceResource, Long>, JpaSpecificationExecutor<FinanceResource> {
}
