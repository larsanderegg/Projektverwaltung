package ch.lan.teko.model;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Activity {

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<DocumentReference> links = new ArrayList<DocumentReference>();

    /**
     */
    @NotNull
    @DateTimeFormat(style = "M-")
    private LocalDate startDate;

    /**
     */
    @NotNull
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
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Resource> resources = new HashSet<Resource>();

    /**
     */
    @NotNull
    @ManyToOne
    private Employee responsible;

    /**
     */
    @NotNull
    private Byte progress;
}
