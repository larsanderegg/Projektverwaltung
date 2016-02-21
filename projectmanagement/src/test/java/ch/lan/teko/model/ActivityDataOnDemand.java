package ch.lan.teko.model;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ActivityDataOnDemand {
	
	private Random rnd = new SecureRandom();
    
    private List<Activity> data;
    
    private EmployeeDataOnDemand employeeDataOnDemand = new EmployeeDataOnDemand();
    
    public Activity getNewTransientActivity(int index) {
        Activity obj = new Activity();
        setEndDate(obj, index);
        setName(obj, index);
        setPhaseId(obj, index);
        setPlanedEndDate(obj, index);
        setPlanedStartDate(obj, index);
        setProgress(obj, index);
        setResponsible(obj, index);
        setStartDate(obj, index);
        return obj;
    }
    
    public void setEndDate(Activity obj, int index) {
        LocalDate endDate = null;
        obj.setEndDate(endDate);
    }
    
    public void setName(Activity obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setPhaseId(Activity obj, int index) {
        Long phaseId = new Integer(index).longValue();
        obj.setPhaseId(phaseId);
    }
    
    public void setPlanedEndDate(Activity obj, int index) {
        LocalDate planedEndDate = LocalDate.now().plusDays(index);
        obj.setPlanedEndDate(planedEndDate);
    }
    
    public void setPlanedStartDate(Activity obj, int index) {
        LocalDate planedStartDate = LocalDate.now().minusDays(index);
        obj.setPlanedStartDate(planedStartDate);
    }
    
    public void setProgress(Activity obj, int index) {
        Byte progress = new Byte("1");
        obj.setProgress(progress);
    }
    
    public void setResponsible(Activity obj, int index) {
        Employee responsible = employeeDataOnDemand.getRandomEmployee();
        obj.setResponsible(responsible);
    }
    
    public void setStartDate(Activity obj, int index) {
        LocalDate startDate = null;
        obj.setStartDate(startDate);
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
        return Activity.findActivity(id);
    }
    
    public Activity getRandomActivity() {
        init();
        Activity obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Activity.findActivity(id);
    }
    
    public boolean modifyActivity(Activity obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = Activity.findActivityEntries(from, to);
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
