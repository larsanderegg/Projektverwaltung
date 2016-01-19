package ch.lan.teko.model;
import ch.lan.teko.repository.PersonalResourceRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = PersonalResource.class)
public class PersonalResourceDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<PersonalResource> data;

	@Autowired
    PersonalResourceRepository personalResourceRepository;

	public PersonalResource getNewTransientPersonalResource(int index) {
        PersonalResource obj = new PersonalResource();
        return obj;
    }

	public PersonalResource getSpecificPersonalResource(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        PersonalResource obj = data.get(index);
        Long id = obj.getId();
        return personalResourceRepository.findOne(id);
    }

	public PersonalResource getRandomPersonalResource() {
        init();
        PersonalResource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return personalResourceRepository.findOne(id);
    }

	public boolean modifyPersonalResource(PersonalResource obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = personalResourceRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'PersonalResource' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<PersonalResource>();
        for (int i = 0; i < 10; i++) {
            PersonalResource obj = getNewTransientPersonalResource(i);
            try {
                personalResourceRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            personalResourceRepository.flush();
            data.add(obj);
        }
    }
}
