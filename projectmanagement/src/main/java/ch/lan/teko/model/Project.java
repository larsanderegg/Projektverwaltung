package ch.lan.teko.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Project {
	
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
    private Byte progress;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate approvalDate;

    /**
     */
    @NotNull
    @Size(min = 2)
    private String name;

    /**
     */
    private String description;

    /**
     */
    @NotNull
    private Byte priority;

    /**
     */
    @NotNull
    private String projectState;

    /**
     */
    @NotNull
    @ManyToOne
    private Employee projectmanager;

    /**
     */
    @NotNull
    @ManyToOne
    private ProcessModel processModel;

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Phase> phases = new ArrayList<Phase>();
    
    public Byte getProgress() {
        return this.progress;
    }

	public void setProgress(Byte progress) {
        this.progress = progress;
    }

	public LocalDate getApprovalDate() {
        return this.approvalDate;
    }

	public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public Byte getPriority() {
        return this.priority;
    }

	public void setPriority(Byte priority) {
        this.priority = priority;
    }

	public String getProjectState() {
        return this.projectState;
    }

	public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

	public Employee getProjectmanager() {
        return this.projectmanager;
    }

	public void setProjectmanager(Employee projectmanager) {
        this.projectmanager = projectmanager;
    }

	public ProcessModel getProcessModel() {
        return this.processModel;
    }

	public void setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
    }

	public List<Phase> getPhases() {
        return this.phases;
    }

	public void setPhases(List<Phase> phases) {
        this.phases = phases;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("progress", "approvalDate", "name", "description", "priority", "projectState", "projectmanager", "processModel", "phases");

	public static final EntityManager entityManager() {
        EntityManager em = new Project().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProjects() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Project o", Long.class).getSingleResult();
    }

	public static List<Project> findAllProjects() {
        return entityManager().createQuery("SELECT o FROM Project o", Project.class).getResultList();
    }

	public static List<Project> findAllProjects(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Project o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Project.class).getResultList();
    }

	public static Project findProject(Long id) {
        if (id == null) return null;
        return entityManager().find(Project.class, id);
    }

	public static List<Project> findProjectEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Project o", Project.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Project> findProjectEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Project o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Project.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static Project findProjectByPhaseId(Long phaseId){
		if (phaseId == null) return null;
		TypedQuery<Project> query = entityManager().createQuery("SELECT o FROM Project o INNER JOIN o.phases p WHERE p.id = :phaseId", Project.class);
		query.setParameter("phaseId", phaseId);
		return query.getSingleResult();
	}
	
	public static Project findProjectByActivityId(Long activityId){
		if (activityId == null) return null;
		TypedQuery<Project> query = entityManager().createQuery("SELECT o FROM Project o INNER JOIN o.phases p INNER JOIN p.activities a WHERE a.id = :activityId", Project.class);
		query.setParameter("activityId", activityId);
		return query.getSingleResult();
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
            Project attached = Project.findProject(this.id);
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
    public Project merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Project merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
