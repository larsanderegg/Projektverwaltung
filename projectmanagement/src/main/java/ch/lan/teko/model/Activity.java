package ch.lan.teko.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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
public class Activity extends PhaseChild implements ISummedResources {

	@PersistenceContext
	transient EntityManager entityManager;
	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("links", "startDate",
			"endDate", "planedStartDate", "planedEndDate", "resources", "responsible", "progress");

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
	@ManyToMany(cascade = CascadeType.ALL)
	private List<DocumentReference> links = new ArrayList<DocumentReference>();

	/**
	 */
	@DateTimeFormat(style = "M-")
	private LocalDate startDate;

	/**
	 */
	@DateTimeFormat(style = "M-")
	private LocalDate endDate;

	/**
	 */
	@NotNull
	@DateTimeFormat(style = "M-")
	private LocalDate planedStartDate;

	/**
	 */
	@NotNull
	@DateTimeFormat(style = "M-")
	private LocalDate planedEndDate;

	/**
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Resource> resources = new HashSet<Resource>();

	/**
	 */
	@NotNull
	@ManyToOne
	private Employee responsible;

	private transient Long phaseId;

	private transient ResourceCollector resourceCollector;
	
	private transient Set<PersonalResource> personalResources;
	
	private transient Set<FinanceResource> financeResources;

	/**
	 */
	@NotNull
	private Byte progress;

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	@Override
	protected LocalDate getDateToCompare() {
		return planedStartDate;
	}

	@Override
	public int compareTo(PhaseChild o) {
		int result = super.compareTo(o);
		if (result == 0) {
			if (!(o instanceof Activity)) {
				result = -1;
			}
		}
		return result;
	}

	@Override
	public ResourceCollector getSummedResources() {
		if (resourceCollector == null) {
			buildInternalResources();
		}
		return resourceCollector;
	}

	private void buildInternalResources() {
		financeResources = new HashSet<>();
		personalResources = new HashSet<>();
		resourceCollector = new ResourceCollector();
		for (Resource resource : resources) {
			resource.fill(resourceCollector);
			
			if(resource instanceof FinanceResource){
				financeResources.add((FinanceResource) resource);
			} else if(resource instanceof PersonalResource) {
				personalResources.add((PersonalResource) resource);
			}
		}
	}

	@Override
	public TimeBoxedData getTimeBoxedData() {
		return new TimeBoxedData(startDate, endDate, planedStartDate, planedEndDate);
	}

	public static void addDocumentReference(Long activityId, DocumentReference documentReference) {
		Activity activity = findActivity(activityId);
		if (activity != null) {
			if (!activity.getLinks().contains(documentReference)) {
				activity.getLinks().add(documentReference);
				activity.merge();
			}
		}
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public List<DocumentReference> getLinks() {
		return this.links;
	}

	public void setLinks(List<DocumentReference> links) {
		this.links = links;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getPlanedStartDate() {
		return this.planedStartDate;
	}

	public void setPlanedStartDate(LocalDate planedStartDate) {
		this.planedStartDate = planedStartDate;
	}

	public LocalDate getPlanedEndDate() {
		return this.planedEndDate;
	}

	public void setPlanedEndDate(LocalDate planedEndDate) {
		this.planedEndDate = planedEndDate;
	}

	public Set<Resource> getResources() {
		return Collections.unmodifiableSet(this.resources);
	}
	
	public void addResource(Resource resourceToAdd){
		this.resources.add(resourceToAdd);
		
		if(financeResources == null || personalResources == null){
			buildInternalResources();
		}
		
		if(resourceToAdd instanceof FinanceResource){
			financeResources.add((FinanceResource) resourceToAdd);
		} else if(resourceToAdd instanceof PersonalResource) {
			personalResources.add((PersonalResource) resourceToAdd);
		}
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public Employee getResponsible() {
		return this.responsible;
	}

	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
	}

	public Byte getProgress() {
		return this.progress;
	}

	public void setProgress(Byte progress) {
		this.progress = progress;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the personalResources
	 */
	public Set<PersonalResource> getPersonalResources() {
		if(personalResources == null){
			buildInternalResources();
		}
		return personalResources;
	}

	/**
	 * @return the financeResources
	 */
	public Set<FinanceResource> getFinanceResources() {
		if(financeResources == null){
			buildInternalResources();
		}
		return financeResources;
	}

	public static final EntityManager entityManager() {
		EntityManager em = new Activity().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countActivitys() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Activity o", Long.class).getSingleResult();
	}

	public static List<Activity> findAllActivitys() {
		return entityManager().createQuery("SELECT o FROM Activity o", Activity.class).getResultList();
	}

	public static List<Activity> findAllActivitys(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Activity o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Activity.class).getResultList();
	}

	public static Activity findActivity(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Activity.class, id);
	}

	public static List<Activity> findActivityEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Activity o", Activity.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Activity> findActivityEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Activity o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Activity.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
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
			Activity attached = Activity.findActivity(this.id);
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
	public Activity merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Activity merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
