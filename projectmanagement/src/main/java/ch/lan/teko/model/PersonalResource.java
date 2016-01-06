package ch.lan.teko.model;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;

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
