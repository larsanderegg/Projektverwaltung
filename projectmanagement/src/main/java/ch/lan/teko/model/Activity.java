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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

/**
 * Represents an activity. Can be stored in a database.
 * @author landeregg
 */
@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Activity extends PhaseChild implements ISummedResources {

	@PersistenceContext
	transient EntityManager entityManager;
	private static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("links", "startDate",
			"endDate", "planedStartDate", "planedEndDate", "resources", "responsible", "progress");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DocumentReference> links = new ArrayList<DocumentReference>();

	@DateTimeFormat(style = "M-")
	private LocalDate startDate;

	@DateTimeFormat(style = "M-")
	private LocalDate endDate;

	@NotNull
	@DateTimeFormat(style = "M-")
	private LocalDate planedStartDate;

	@NotNull
	@DateTimeFormat(style = "M-")
	private LocalDate planedEndDate;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Resource> resources = new HashSet<Resource>();

	@NotNull
	@ManyToOne
	private Employee responsible;
	
	@NotNull
	private Byte progress;

	private transient Long phaseId;

	private transient ResourceCollector resourceCollector;
	
	private transient Set<PersonalResource> personalResources;
	
	private transient Set<FinanceResource> financeResources;


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDate getDateToCompare() {
		return planedStartDate;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TimeBoxedData getTimeBoxedData() {
		return new TimeBoxedData(startDate, endDate, planedStartDate, planedEndDate);
	}

	/**
	 * @param activityId
	 * @param documentReference
	 */
	public static void addDocumentReference(Long activityId, DocumentReference documentReference) {
		Activity activity = findActivity(activityId);
		if (activity != null) {
			if (!activity.getLinks().contains(documentReference)) {
				activity.getLinks().add(documentReference);
				activity.merge();
			}
		}
	}
	
	/**
	 * @return the links
	 */
	public List<DocumentReference> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(List<DocumentReference> links) {
		this.links = links;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the planedStartDate
	 */
	public LocalDate getPlanedStartDate() {
		return planedStartDate;
	}

	/**
	 * @param planedStartDate the planedStartDate to set
	 */
	public void setPlanedStartDate(LocalDate planedStartDate) {
		this.planedStartDate = planedStartDate;
	}

	/**
	 * @return the planedEndDate
	 */
	public LocalDate getPlanedEndDate() {
		return planedEndDate;
	}

	/**
	 * @param planedEndDate the planedEndDate to set
	 */
	public void setPlanedEndDate(LocalDate planedEndDate) {
		this.planedEndDate = planedEndDate;
	}

	/**
	 * @return the phaseId
	 */
	public Long getPhaseId() {
		return phaseId;
	}

	/**
	 * @param phaseId the phaseId to set
	 */
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	/**
	 * @return the resources as a unmodifiable set
	 */
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
	
	public void removeResource(Resource resourceToRemove){
		this.resources.remove(resourceToRemove);
		
		if(financeResources == null || personalResources == null){
			buildInternalResources();
		}
		
		if(resourceToRemove instanceof FinanceResource){
			financeResources.remove((FinanceResource) resourceToRemove);
		} else if(resourceToRemove instanceof PersonalResource) {
			personalResources.remove((PersonalResource) resourceToRemove);
		}
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
		buildInternalResources();
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
	 * @return the responsible
	 */
	public Employee getResponsible() {
		return responsible;
	}

	/**
	 * @param responsible the responsible to set
	 */
	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
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
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Creates an entity manager to access a database.
	 * @return an {@link EntityManager}
	 */
	public static final EntityManager entityManager() {
		EntityManager em = new Activity().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	/**
	 * @return the amount of activities in the database
	 */
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
