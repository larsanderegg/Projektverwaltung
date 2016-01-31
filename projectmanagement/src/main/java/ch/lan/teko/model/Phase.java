package ch.lan.teko.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Phase {
	
	@PersistenceContext
    transient EntityManager entityManager;
	
	/**
     */
    @NotNull
    private String name;
	
	/**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<DocumentReference> links = new HashSet<DocumentReference>();

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate reviewDate;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate approvalDate;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate planedReviewDate;

    /**
     */
    private Byte progress;

    /**
     */
    private String phaseState;
    
    private transient Long projectId;

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<Activity>();
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

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

	public Set<DocumentReference> getLinks() {
        return this.links;
    }

	public void setLinks(Set<DocumentReference> links) {
        this.links = links;
    }

	public LocalDate getReviewDate() {
        return this.reviewDate;
    }

	public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

	public LocalDate getApprovalDate() {
        return this.approvalDate;
    }

	public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

	public LocalDate getPlanedReviewDate() {
        return this.planedReviewDate;
    }

	public void setPlanedReviewDate(LocalDate planedReviewDate) {
        this.planedReviewDate = planedReviewDate;
    }

	public Byte getProgress() {
        return this.progress;
    }

	public void setProgress(Byte progress) {
        this.progress = progress;
    }

	public String getPhaseState() {
        return this.phaseState;
    }

	public void setPhaseState(String phaseState) {
        this.phaseState = phaseState;
    }

	public List<Activity> getActivities() {
        return this.activities;
    }

	public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String toString() {
        return name;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("links", "reviewDate", "approvalDate", "planedReviewDate", "progress", "phaseState", "activities");

	public static final EntityManager entityManager() {
        EntityManager em = new Phase().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPhases() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Phase o", Long.class).getSingleResult();
    }

	public static List<Phase> findAllPhases() {
        return entityManager().createQuery("SELECT o FROM Phase o", Phase.class).getResultList();
    }

	public static List<Phase> findAllPhases(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Phase o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Phase.class).getResultList();
    }

	public static Phase findPhase(Long id) {
        if (id == null) return null;
        return entityManager().find(Phase.class, id);
    }

	public static List<Phase> findPhaseEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Phase o", Phase.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Phase> findPhaseEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Phase o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Phase.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Phase attached = Phase.findPhase(this.id);
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
    public Phase merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Phase merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
