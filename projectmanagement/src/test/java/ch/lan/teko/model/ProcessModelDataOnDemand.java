package ch.lan.teko.model;
import ch.lan.teko.repository.ProcessModelRepository;
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
@RooDataOnDemand(entity = ProcessModel.class)
public class ProcessModelDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ProcessModel> data;

	@Autowired
    ProcessModelRepository processModelRepository;

	public ProcessModel getNewTransientProcessModel(int index) {
        ProcessModel obj = new ProcessModel();
        return obj;
    }

	public ProcessModel getSpecificProcessModel(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProcessModel obj = data.get(index);
        Long id = obj.getId();
        return processModelRepository.findOne(id);
    }

	public ProcessModel getRandomProcessModel() {
        init();
        ProcessModel obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return processModelRepository.findOne(id);
    }

	public boolean modifyProcessModel(ProcessModel obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = processModelRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProcessModel' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ProcessModel>();
        for (int i = 0; i < 10; i++) {
            ProcessModel obj = getNewTransientProcessModel(i);
            try {
                processModelRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            processModelRepository.flush();
            data.add(obj);
        }
    }
}
