package ch.lan.teko.repository;
import ch.lan.teko.model.PersonalResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = PersonalResource.class)
public interface PersonalResourceRepository extends JpaSpecificationExecutor<PersonalResource>, JpaRepository<PersonalResource, Long> {
}
