// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Milestone;
import ch.lan.teko.model.MilestoneDataOnDemand;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect MilestoneDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MilestoneDataOnDemand: @Component;
    
    private Random MilestoneDataOnDemand.rnd = new SecureRandom();
    
    private List<Milestone> MilestoneDataOnDemand.data;
    
    public Milestone MilestoneDataOnDemand.getNewTransientMilestone(int index) {
        Milestone obj = new Milestone();
        setName(obj, index);
        setPlanedDate(obj, index);
        return obj;
    }
    
    public void MilestoneDataOnDemand.setName(Milestone obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void MilestoneDataOnDemand.setPlanedDate(Milestone obj, int index) {
        LocalDate planedDate = null;
        obj.setPlanedDate(planedDate);
    }
    
    public Milestone MilestoneDataOnDemand.getSpecificMilestone(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Milestone obj = data.get(index);
        Long id = obj.getId();
        return Milestone.findMilestone(id);
    }
    
    public Milestone MilestoneDataOnDemand.getRandomMilestone() {
        init();
        Milestone obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Milestone.findMilestone(id);
    }
    
    public boolean MilestoneDataOnDemand.modifyMilestone(Milestone obj) {
        return false;
    }
    
    public void MilestoneDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Milestone.findMilestoneEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Milestone' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Milestone>();
        for (int i = 0; i < 10; i++) {
            Milestone obj = getNewTransientMilestone(i);
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