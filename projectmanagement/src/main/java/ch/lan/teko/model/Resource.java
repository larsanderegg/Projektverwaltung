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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents the similarities of external costs and personal efforts. Can be stored in a database as either a external cost or a personal effort.
 * @author landeregg
 */
@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public abstract class Resource {
	
	@PersistenceContext
    transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("planed", "effectiv", "explanation");
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;
	
	@NotNull
    private Integer planed;

    private Integer effectiv;

    private String explanation;
    
	private transient Long activityId;
	
	/**
	 * Fill the given collector with data
	 * @param collector the collector to fill
	 */
	public abstract void fill(ResourceCollector collector);
	
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
	 * @return the planed
	 */
	public Integer getPlaned() {
		return planed;
	}

	/**
	 * @param planed the planed to set
	 */
	public void setPlaned(Integer planed) {
		this.planed = planed;
	}

	/**
	 * @return the effectiv
	 */
	public Integer getEffectiv() {
		return effectiv;
	}

	/**
	 * @param effectiv the effectiv to set
	 */
	public void setEffectiv(Integer effectiv) {
		this.effectiv = effectiv;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return the activityId
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
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
        EntityManager em = new Resource() {

			@Override
			public void fill(ResourceCollector collector) {
				// do nothing
			}
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countResources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Resource o", Long.class).getSingleResult();
    }

	public static List<Resource> findAllResources() {
        return entityManager().createQuery("SELECT o FROM Resource o", Resource.class).getResultList();
    }

	public static List<Resource> findAllResources(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Resource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Resource.class).getResultList();
    }

	public static Resource findResource(Long id) {
        if (id == null) return null;
        return entityManager().find(Resource.class, id);
    }

	public static List<Resource> findResourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Resource o", Resource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Resource> findResourceEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Resource o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Resource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Resource attached = Resource.findResource(this.id);
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
    public Resource merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Resource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
