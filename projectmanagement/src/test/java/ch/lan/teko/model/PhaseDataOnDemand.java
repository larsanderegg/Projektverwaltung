package ch.lan.teko.model;
import ch.lan.teko.repository.PhaseRepository;
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

@Configurable
@Component
@RooDataOnDemand(entity = Phase.class)
public class PhaseDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Phase> data;

	@Autowired
    PhaseRepository phaseRepository;

	public Phase getNewTransientPhase(int index) {
        Phase obj = new Phase();
        return obj;
    }

	public Phase getSpecificPhase(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Phase obj = data.get(index);
        Long id = obj.getId();
        return phaseRepository.findOne(id);
    }

	public Phase getRandomPhase() {
        init();
        Phase obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return phaseRepository.findOne(id);
    }

	public boolean modifyPhase(Phase obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = phaseRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Phase' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Phase>();
        for (int i = 0; i < 10; i++) {
            Phase obj = getNewTransientPhase(i);
            try {
                phaseRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            phaseRepository.flush();
            data.add(obj);
        }
    }
}
