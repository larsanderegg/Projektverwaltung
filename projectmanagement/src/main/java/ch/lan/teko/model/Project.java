package ch.lan.teko.model;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Project {

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
}
