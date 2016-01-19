package ch.lan.teko.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = FinanceResource.class)
public class FinanceResourceDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<FinanceResource> data;

	public FinanceResource getNewTransientFinanceResource(int index) {
        FinanceResource obj = new FinanceResource();
        return obj;
    }

	public FinanceResource getSpecificFinanceResource(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        FinanceResource obj = data.get(index);
        Long id = obj.getId();
        return FinanceResource.findFinanceResource(id);
    }

	public FinanceResource getRandomFinanceResource() {
        init();
        FinanceResource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return FinanceResource.findFinanceResource(id);
    }

	public boolean modifyFinanceResource(FinanceResource obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = FinanceResource.findFinanceResourceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'FinanceResource' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<FinanceResource>();
        for (int i = 0; i < 10; i++) {
            FinanceResource obj = getNewTransientFinanceResource(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
