package ch.lan.teko.model;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
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
 * Represents a process model. Can be stored in a database.
 * @author landeregg
 */
@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ProcessModel {
	
	@PersistenceContext
    transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "phases");
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;
	
	/**
     */
    @NotNull
    @Size(min = 2)
    private String name;

    /**
     */
    @NotNull
    private String phases;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phases
	 */
	public String getPhases() {
		return phases;
	}

	/**
	 * @param phases the phases to set
	 */
	public void setPhases(String phases) {
		this.phases = phases;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	/**
	 * Creates an entity manager to access a database.
	 * @return an {@link EntityManager}
	 */
	public static final EntityManager entityManager() {
        EntityManager em = new ProcessModel().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProcessModels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProcessModel o", Long.class).getSingleResult();
    }

	public static List<ProcessModel> findAllProcessModels() {
        return entityManager().createQuery("SELECT o FROM ProcessModel o", ProcessModel.class).getResultList();
    }

	public static List<ProcessModel> findAllProcessModels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProcessModel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProcessModel.class).getResultList();
    }

	public static ProcessModel findProcessModel(Long id) {
        if (id == null) return null;
        return entityManager().find(ProcessModel.class, id);
    }

	public static List<ProcessModel> findProcessModelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProcessModel o", ProcessModel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ProcessModel> findProcessModelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProcessModel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProcessModel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ProcessModel attached = ProcessModel.findProcessModel(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public ProcessModel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProcessModel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
