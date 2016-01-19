package ch.lan.teko.model;
import ch.lan.teko.repository.ActivityRepository;
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
@RooDataOnDemand(entity = Activity.class)
public class ActivityDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Activity> data;

	@Autowired
    ActivityRepository activityRepository;

	public Activity getNewTransientActivity(int index) {
        Activity obj = new Activity();
        return obj;
    }

	public Activity getSpecificActivity(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Activity obj = data.get(index);
        Long id = obj.getId();
        return activityRepository.findOne(id);
    }

	public Activity getRandomActivity() {
        init();
        Activity obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return activityRepository.findOne(id);
    }

	public boolean modifyActivity(Activity obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = activityRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Activity' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Activity>();
        for (int i = 0; i < 10; i++) {
            Activity obj = getNewTransientActivity(i);
            try {
                activityRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            activityRepository.flush();
            data.add(obj);
        }
    }
}
