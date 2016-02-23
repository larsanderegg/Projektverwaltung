package ch.lan.teko.model;
import java.time.LocalDate;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents a milestone. Can be stored in a database.
 * @author landeregg
 */
@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Milestone extends PhaseChild{
	
	@PersistenceContext
    transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "planedDate");
	
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
    @DateTimeFormat(style = "M-")
    private LocalDate planedDate;
    
    /**
     */
    private transient Long phaseId;
	
	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	
	@Override
	public TimeBoxedData getTimeBoxedData() {
		return new TimeBoxedData(null, null, planedDate, planedDate);
	}
	
	@Override
	public LocalDate getDateToCompare() {
		return planedDate;
	}

	/**
	 * @param planedDate the planedDate to set
	 */
	public void setPlanedDate(LocalDate planedDate) {
		this.planedDate = planedDate;
	}
	
	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public LocalDate getPlanedDate() {
        return this.planedDate;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
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
        EntityManager em = new Milestone().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMilestones() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Milestone o", Long.class).getSingleResult();
    }

	public static List<Milestone> findAllMilestones() {
        return entityManager().createQuery("SELECT o FROM Milestone o", Milestone.class).getResultList();
    }

	public static List<Milestone> findAllMilestones(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Milestone o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Milestone.class).getResultList();
    }

	public static Milestone findMilestone(Long id) {
        if (id == null) return null;
        return entityManager().find(Milestone.class, id);
    }

	public static List<Milestone> findMilestoneEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Milestone o", Milestone.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Milestone> findMilestoneEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Milestone o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Milestone.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Milestone attached = Milestone.findMilestone(this.id);
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
    public Milestone merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Milestone merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
