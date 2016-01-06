package ch.lan.teko.model;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Phase {

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
    @NotNull
    @DateTimeFormat(style = "M-")
    private LocalDate planedReviewDate;

    /**
     */
    @NotNull
    private Byte progress;

    /**
     */
    @NotNull
    private String phaseState;

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<Activity>();
}
