package ch.lan.teko.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class PersonalResourceDataOnDemand {
	
	private Random rnd = new SecureRandom();
    
    private List<PersonalResource> data;
    
    private EmployeeDataOnDemand employeeDataOnDemand = new EmployeeDataOnDemand();
    
    public PersonalResource getNewTransientPersonalResource(int index) {
        PersonalResource obj = new PersonalResource();
        setActivityId(obj, index);
        setEffectiv(obj, index);
        setEmployee(obj, index);
        setExplanation(obj, index);
        setJob(obj, index);
        setPlaned(obj, index);
        return obj;
    }
    
    public void setActivityId(PersonalResource obj, int index) {
        Long activityId = new Integer(index).longValue();
        obj.setActivityId(activityId);
    }
    
    public void setEffectiv(PersonalResource obj, int index) {
        Integer effectiv = new Integer(index);
        obj.setEffectiv(effectiv);
    }
    
    public void setEmployee(PersonalResource obj, int index) {
        Employee employee = employeeDataOnDemand.getRandomEmployee();
        obj.setEmployee(employee);
    }
    
    public void setExplanation(PersonalResource obj, int index) {
        String explanation = "explanation_" + index;
        obj.setExplanation(explanation);
    }
    
    public void setJob(PersonalResource obj, int index) {
        String job = "job_" + index;
        obj.setJob(job);
    }
    
    public void setPlaned(PersonalResource obj, int index) {
        Integer planed = new Integer(index);
        obj.setPlaned(planed);
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
        return PersonalResource.findPersonalResource(id);
    }
    
    public PersonalResource getRandomPersonalResource() {
        init();
        PersonalResource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return PersonalResource.findPersonalResource(id);
    }
    
    public boolean modifyPersonalResource(PersonalResource obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = PersonalResource.findPersonalResourceEntries(from, to);
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
