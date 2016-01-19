package ch.lan.teko.model;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PersonalResource extends Resource {
	
	/**
     */
    @NotNull
    @Size(min = 2)
    private String job;

    /**
     */
    @NotNull
    @ManyToOne
    private Employee employee;
}
