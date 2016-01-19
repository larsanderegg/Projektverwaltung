package ch.lan.teko.repository;
import ch.lan.teko.model.ProcessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = ProcessModel.class)
public interface ProcessModelRepository extends JpaSpecificationExecutor<ProcessModel>, JpaRepository<ProcessModel, Long> {
}
