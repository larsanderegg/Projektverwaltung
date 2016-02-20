package ch.lan.teko.model;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PersonalResource extends Resource {
	
	/**
     */
    @NotNull
    @Size(min = 2)
    private String job;

    /**
     */
    @NotNull
    @ManyToOne
    private Employee employee;
    
	@Override
	public void fill(ResourceCollector collector) {
		collector.incrementPlanedPersonalResources(getPlaned());
		collector.incrementEffectivPersonalResources(getEffectiv());
	}
	
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("job", "employee");

	public static long countPersonalResources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PersonalResource o", Long.class).getSingleResult();
    }

	public static List<PersonalResource> findAllPersonalResources() {
        return entityManager().createQuery("SELECT o FROM PersonalResource o", PersonalResource.class).getResultList();
    }

	public static List<PersonalResource> findAllPersonalResources(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PersonalResource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PersonalResource.class).getResultList();
    }

	public static PersonalResource findPersonalResource(Long id) {
        if (id == null) return null;
        return entityManager().find(PersonalResource.class, id);
    }

	public static List<PersonalResource> findPersonalResourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PersonalResource o", PersonalResource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<PersonalResource> findPersonalResourceEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PersonalResource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PersonalResource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public PersonalResource merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PersonalResource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getJob() {
        return this.job;
    }

	public void setJob(String job) {
        this.job = job;
    }

	public Employee getEmployee() {
        return this.employee;
    }

	public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
