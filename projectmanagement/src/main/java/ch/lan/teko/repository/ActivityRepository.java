package ch.lan.teko.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import ch.lan.teko.model.Activity;

@Repository
@RooJpaRepository(domainType = Activity.class)
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
}
