package ch.lan.teko.repository;
import ch.lan.teko.model.DocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = DocumentReference.class)
public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, Long>, JpaSpecificationExecutor<DocumentReference> {
}
