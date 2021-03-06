// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Employee;
import ch.lan.teko.model.EmployeeDataOnDemand;
import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.model.ProcessModelDataOnDemand;
import ch.lan.teko.model.Project;
import ch.lan.teko.model.ProjectDataOnDemand;
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

privileged aspect ProjectDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ProjectDataOnDemand: @Component;
    
    private Random ProjectDataOnDemand.rnd = new SecureRandom();
    
    private List<Project> ProjectDataOnDemand.data;
    
    @Autowired
    ProcessModelDataOnDemand ProjectDataOnDemand.processModelDataOnDemand;
    
    @Autowired
    EmployeeDataOnDemand ProjectDataOnDemand.employeeDataOnDemand;
    
    public Project ProjectDataOnDemand.getNewTransientProject(int index) {
        Project obj = new Project();
        setApprovalDate(obj, index);
        setDescription(obj, index);
        setName(obj, index);
        setPriority(obj, index);
        setProcessModel(obj, index);
        setProgress(obj, index);
        setProjectState(obj, index);
        setProjectmanager(obj, index);
        return obj;
    }
    
    public void ProjectDataOnDemand.setApprovalDate(Project obj, int index) {
        LocalDate approvalDate = null;
        obj.setApprovalDate(approvalDate);
    }
    
    public void ProjectDataOnDemand.setDescription(Project obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void ProjectDataOnDemand.setName(Project obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void ProjectDataOnDemand.setPriority(Project obj, int index) {
        Byte priority = new Byte("1");
        obj.setPriority(priority);
    }
    
    public void ProjectDataOnDemand.setProcessModel(Project obj, int index) {
        ProcessModel processModel = processModelDataOnDemand.getRandomProcessModel();
        obj.setProcessModel(processModel);
    }
    
    public void ProjectDataOnDemand.setProgress(Project obj, int index) {
        Byte progress = new Byte("1");
        obj.setProgress(progress);
    }
    
    public void ProjectDataOnDemand.setProjectState(Project obj, int index) {
        String projectState = "projectState_" + index;
        obj.setProjectState(projectState);
    }
    
    public void ProjectDataOnDemand.setProjectmanager(Project obj, int index) {
        Employee projectmanager = employeeDataOnDemand.getRandomEmployee();
        obj.setProjectmanager(projectmanager);
    }
    
    public Project ProjectDataOnDemand.getSpecificProject(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Project obj = data.get(index);
        Long id = obj.getId();
        return Project.findProject(id);
    }
    
    public Project ProjectDataOnDemand.getRandomProject() {
        init();
        Project obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Project.findProject(id);
    }
    
    public boolean ProjectDataOnDemand.modifyProject(Project obj) {
        return false;
    }
    
    public void ProjectDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Project.findProjectEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Project' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Project>();
        for (int i = 0; i < 10; i++) {
            Project obj = getNewTransientProject(i);
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
