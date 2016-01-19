package ch.lan.teko.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

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
