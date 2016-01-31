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

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class DocumentReference {
	
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
    @Size(min = 2)
    private String path;
    
    private transient Long activityId;
    private transient Long phaseId;
    private transient Long projectId;
    
	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getPath() {
        return this.path;
    }

	public void setPath(String path) {
        this.path = path;
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
	
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "path");

	public static final EntityManager entityManager() {
        EntityManager em = new DocumentReference().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDocumentReferences() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DocumentReference o", Long.class).getSingleResult();
    }

	public static List<DocumentReference> findAllDocumentReferences() {
        return entityManager().createQuery("SELECT o FROM DocumentReference o", DocumentReference.class).getResultList();
    }

	public static List<DocumentReference> findAllDocumentReferences(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DocumentReference o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DocumentReference.class).getResultList();
    }

	public static DocumentReference findDocumentReference(Long id) {
        if (id == null) return null;
        return entityManager().find(DocumentReference.class, id);
    }

	public static List<DocumentReference> findDocumentReferenceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DocumentReference o", DocumentReference.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<DocumentReference> findDocumentReferenceEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DocumentReference o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DocumentReference.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            DocumentReference attached = DocumentReference.findDocumentReference(this.id);
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
    public DocumentReference merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DocumentReference merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
