package ch.lan.teko.model;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public abstract class Resource {
	
	/**
     */
	@NotNull
    private Integer planed;

    /**
     */
	@NotNull
    private Integer effectiv;

    /**
     */
    private String explanation;
}
