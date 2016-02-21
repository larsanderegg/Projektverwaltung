package ch.lan.teko.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class EmployeeDataOnDemand {
	
private Random rnd = new SecureRandom();
    
    private List<Employee> data;
    
    public Employee getNewTransientEmployee(int index) {
        Employee obj = new Employee();
        setJob(obj, index);
        setName(obj, index);
        setPensum(obj, index);
        setSurname(obj, index);
        return obj;
    }
    
    public void setJob(Employee obj, int index) {
        String job = "job_" + index;
        obj.setJob(job);
    }
    
    public void setName(Employee obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setPensum(Employee obj, int index) {
        Byte pensum = new Byte("1");
        obj.setPensum(pensum);
    }
    
    public void setSurname(Employee obj, int index) {
        String surname = "surname_" + index;
        obj.setSurname(surname);
    }
    
    public Employee getSpecificEmployee(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Employee obj = data.get(index);
        Long id = obj.getId();
        return Employee.findEmployee(id);
    }
    
    public Employee getRandomEmployee() {
        init();
        Employee obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Employee.findEmployee(id);
    }
    
    public boolean modifyEmployee(Employee obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = Employee.findEmployeeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Employee' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Employee>();
        for (int i = 0; i < 10; i++) {
            Employee obj = getNewTransientEmployee(i);
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
