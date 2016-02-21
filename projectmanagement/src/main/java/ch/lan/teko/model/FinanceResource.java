package ch.lan.teko.model;
import java.util.List;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents an external cost. Can be stored in a database.
 * @author landeregg
 */
@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class FinanceResource extends Resource {
	
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("type");
	
	/**
     */
    @NotNull
    @Size(min = 2)
    private String type;
	
	@Override
	public void fill(ResourceCollector collector) {
		collector.incrementPlanedFinanceResources(getPlaned());
		collector.incrementEffectivFinanceResources(getEffectiv());
	}
	
	public String getType() {
        return this.type;
    }

	public void setType(String type) {
        this.type = type;
    }
	
	public static long countFinanceResources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FinanceResource o", Long.class).getSingleResult();
    }

	public static List<FinanceResource> findAllFinanceResources() {
        return entityManager().createQuery("SELECT o FROM FinanceResource o", FinanceResource.class).getResultList();
    }

	public static List<FinanceResource> findAllFinanceResources(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FinanceResource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FinanceResource.class).getResultList();
    }

	public static FinanceResource findFinanceResource(Long id) {
        if (id == null) return null;
        return entityManager().find(FinanceResource.class, id);
    }

	public static List<FinanceResource> findFinanceResourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FinanceResource o", FinanceResource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<FinanceResource> findFinanceResourceEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FinanceResource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FinanceResource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public FinanceResource merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FinanceResource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
