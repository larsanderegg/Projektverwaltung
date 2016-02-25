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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

/**
 * Represents a project. Can be stored in a database.
 * @author landeregg
 */
@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Project implements ISummedResources, ITimeBoxed {

	@PersistenceContext
	transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("progress", "approvalDate",
			"name", "description", "priority", "projectState", "projectmanager", "processModel", "phases");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	private Byte progress;

	@DateTimeFormat(style = "M-")
	private LocalDate approvalDate;

	@NotNull
	@Size(min = 2)
	private String name;

	private String description;

	@NotNull
	private Byte priority;

	@NotNull
	private String projectState;

	@NotNull
	@ManyToOne
	private Employee projectmanager;

	@NotNull
	@ManyToOne
	private ProcessModel processModel;

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	private List<Phase> phases = new ArrayList<Phase>();

	private transient ResourceCollector resourceCollector;

	private transient TimeBoxedData timeBoxedData;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceCollector getSummedResources() {
		if (resourceCollector == null) {
			buildInternalResources();
		}
		return resourceCollector;
	}

	private void buildInternalResources() {
		resourceCollector = new ResourceCollector();
		for (Phase phase : phases) {
			resourceCollector.increment(phase.getSummedResources());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TimeBoxedData getTimeBoxedData() {
		if (timeBoxedData == null) {
			buildTimeBoxedData();
		}
		return timeBoxedData;
	}

	private void buildTimeBoxedData() {
		timeBoxedData = new TimeBoxedData();
		for (Phase phase : phases) {
			timeBoxedData.add(phase);
		}
	}

	/**
	 * NOT IMPLEMENTED 
	 * @param projectId the project id to use
	 * @param documentReference the document reference to add
	 */
	public static void addDocumentReference(Long projectId, DocumentReference documentReference) {
		//TODO implement if needed
		// Project project = findProject(projectId);
		// if(project != null){
		// project.getLinks().add(documentReference);
		// project.merge();
		// }
		System.out.println("not implemented");
	}
	
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
	 * @return the progress
	 */
	public Byte getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(Byte progress) {
		this.progress = progress;
	}

	/**
	 * @return the approvalDate
	 */
	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the priority
	 */
	public Byte getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Byte priority) {
		this.priority = priority;
	}

	/**
	 * @return the projectState
	 */
	public String getProjectState() {
		return projectState;
	}

	/**
	 * @param projectState the projectState to set
	 */
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	/**
	 * @return the projectmanager
	 */
	public Employee getProjectmanager() {
		return projectmanager;
	}

	/**
	 * @param projectmanager the projectmanager to set
	 */
	public void setProjectmanager(Employee projectmanager) {
		this.projectmanager = projectmanager;
	}

	/**
	 * @return the processModel
	 */
	public ProcessModel getProcessModel() {
		return processModel;
	}

	/**
	 * @param processModel the processModel to set
	 */
	public void setProcessModel(ProcessModel processModel) {
		this.processModel = processModel;
	}

	/**
	 * @return the phases
	 */
	public List<Phase> getPhases() {
		return phases;
	}

	/**
	 * @param phases the phases to set
	 */
	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	/**
	 * @return the resourceCollector
	 */
	public ResourceCollector getResourceCollector() {
		return resourceCollector;
	}

	/**
	 * @param resourceCollector the resourceCollector to set
	 */
	public void setResourceCollector(ResourceCollector resourceCollector) {
		this.resourceCollector = resourceCollector;
	}

	/**
	 * Creates an entity manager to access a database.
	 * @return an {@link EntityManager}
	 */
	public static final EntityManager entityManager() {
		EntityManager em = new Project().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static Project findProjectByPhaseId(Long phaseId) {
		if (phaseId == null)
			return null;
		TypedQuery<Project> query = entityManager()
				.createQuery("SELECT o FROM Project o INNER JOIN o.phases p WHERE p.id = :phaseId", Project.class);
		query.setParameter("phaseId", phaseId);
		return query.getSingleResult();
	}

	public static Project findProjectByActivityId(Long activityId) {
		if (activityId == null)
			return null;
		TypedQuery<Project> query = entityManager().createQuery(
				"SELECT o FROM Project o INNER JOIN o.phases p INNER JOIN p.activities a WHERE a.id = :activityId",
				Project.class);
		query.setParameter("activityId", activityId);
		return query.getSingleResult();
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
		if (id == null)
			return null;
		return entityManager().find(Project.class, id);
	}

	public static List<Project> findProjectEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Project o", Project.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Project> findProjectEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Project o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Project.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();

		List<Phase> generatePhases = Phase.generatePhases(this.getProcessModel());
		this.setPhases(generatePhases);

		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Project attached = Project.findProject(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Project merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Project merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
