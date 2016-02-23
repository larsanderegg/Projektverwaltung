package ch.lan.teko.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents a project phase. Can be stored in a database.
 * @author landeregg
 */
@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Phase implements ISummedResources, ITimeBoxed {

	@PersistenceContext
	transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("links", "reviewDate",
			"approvalDate", "planedReviewDate", "progress", "phaseState", "activities");

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
	private String name;

	/**
	 */
	@OneToMany(cascade = CascadeType.ALL)
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

	/**
	 */
	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Activity> activities = new HashSet<Activity>();

	/**
	 */
	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Milestone> milestones = new HashSet<>();

	private transient Long projectId;

	private transient SortedSet<PhaseChild> childs;

	private transient ResourceCollector resourceCollector;

	private transient TimeBoxedData timeBoxedData;

	private transient Milestone endMilestone;

	public Set<Activity> getActivities() {
		return Collections.unmodifiableSet(activities);
	}

	public void addActivity(Activity add) {
		activities.add(add);
		if (childs == null) {
			buildChilds();
		} else {
			childs.add(add);
		}
	}

	public void removeActivity(Activity remove) {
		activities.remove(remove);
		if (childs != null) {
			childs.remove(remove);
		}
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
		buildChilds();
	}

	public Set<Milestone> getMilestones() {
		return Collections.unmodifiableSet(milestones);
	}

	public void addMilestone(Milestone add) {
		milestones.add(add);
		if (childs == null) {
			buildChilds();
		} else {
			childs.add(add);
		}
	}

	public void removeMilestone(Milestone remove) {
		milestones.remove(remove);
		if (childs != null) {
			childs.remove(remove);
		}
	}

	public void setMilestones(Set<Milestone> milestones) {
		this.milestones = milestones;
		buildChilds();
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the endMilestone
	 */
	public Milestone getEndMilestone() {
		if (endMilestone == null) {
			endMilestone = new Milestone();
			endMilestone.setPlanedDate(getTimeBoxedData().getPlanedEndDate());
		}
		return endMilestone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}

	public SortedSet<PhaseChild> getChilds() {
		if (childs == null) {
			buildChilds();
		}
		return childs;
	}

	private void buildChilds() {
		childs = new TreeSet<>();
		childs.addAll(activities);
		childs.addAll(milestones);
	}

	@Override
	public ResourceCollector getSummedResources() {
		if (resourceCollector == null) {
			buildInternalResources();
		}
		return resourceCollector;
	}

	private void buildInternalResources() {
		resourceCollector = new ResourceCollector();
		for (Activity activity : activities) {
			resourceCollector.increment(activity.getSummedResources());
		}
	}

	@Override
	public TimeBoxedData getTimeBoxedData() {
		if (timeBoxedData == null) {
			buildTimeBoxedData();
		}
		return timeBoxedData;
	}

	private void buildTimeBoxedData() {
		timeBoxedData = new TimeBoxedData();
		SortedSet<PhaseChild> tempChilds = getChilds();
		for (PhaseChild child : tempChilds) {
			timeBoxedData.add(child);
		}
	}

	public static List<Phase> generatePhases(ProcessModel processModel) {
		List<Phase> result = new ArrayList<>();

		String[] phaseNames = processModel.getPhases().split(";");
		for (String name : phaseNames) {
			Phase phase = new Phase();
			phase.setName(name);
			result.add(phase);
		}
		return result;
	}

	public static void addDocumentReference(Long phaseId, DocumentReference documentReference) {
		Phase phase = findPhase(phaseId);
		if (phase != null) {
			if (!phase.getLinks().contains(documentReference)) {
				phase.getLinks().add(documentReference);
				phase.merge();
			}
		}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Creates an entity manager to access a database.
	 * @return an {@link EntityManager}
	 */
	public static final EntityManager entityManager() {
		EntityManager em = new Phase().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
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
		if (id == null)
			return null;
		return entityManager().find(Phase.class, id);
	}

	public static List<Phase> findPhaseEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Phase o", Phase.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Phase> findPhaseEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Phase o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Phase.class).setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Phase attached = Phase.findPhase(this.id);
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
	public Phase merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Phase merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
