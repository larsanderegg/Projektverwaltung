// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.ActivityDataOnDemand;
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.EmployeeDataOnDemand;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect ActivityDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ActivityDataOnDemand: @Component;
    
    private Random ActivityDataOnDemand.rnd = new SecureRandom();
    
    private List<Activity> ActivityDataOnDemand.data;
    
    @Autowired
    EmployeeDataOnDemand ActivityDataOnDemand.employeeDataOnDemand;
    
    public Activity ActivityDataOnDemand.getNewTransientActivity(int index) {
        Activity obj = new Activity();
        setEndDate(obj, index);
        setPlanedEndDate(obj, index);
        setPlanedStartDate(obj, index);
        setProgress(obj, index);
        setResponsible(obj, index);
        setStartDate(obj, index);
        return obj;
    }
    
    public void ActivityDataOnDemand.setEndDate(Activity obj, int index) {
        LocalDate endDate = null;
        obj.setEndDate(endDate);
    }
    
    public void ActivityDataOnDemand.setPlanedEndDate(Activity obj, int index) {
        LocalDate planedEndDate = null;
        obj.setPlanedEndDate(planedEndDate);
    }
    
    public void ActivityDataOnDemand.setPlanedStartDate(Activity obj, int index) {
        LocalDate planedStartDate = null;
        obj.setPlanedStartDate(planedStartDate);
    }
    
    public void ActivityDataOnDemand.setProgress(Activity obj, int index) {
        Byte progress = new Byte("1");
        obj.setProgress(progress);
    }
    
    public void ActivityDataOnDemand.setResponsible(Activity obj, int index) {
        Employee responsible = employeeDataOnDemand.getRandomEmployee();
        obj.setResponsible(responsible);
    }
    
    public void ActivityDataOnDemand.setStartDate(Activity obj, int index) {
        LocalDate startDate = null;
        obj.setStartDate(startDate);
    }
    
    public Activity ActivityDataOnDemand.getSpecificActivity(int index) {
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
    
    public Activity ActivityDataOnDemand.getRandomActivity() {
        init();
        Activity obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Activity.findActivity(id);
    }
    
    public boolean ActivityDataOnDemand.modifyActivity(Activity obj) {
        return false;
    }
    
    public void ActivityDataOnDemand.init() {
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
