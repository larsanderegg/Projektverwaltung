// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Employee;
import ch.lan.teko.model.PersonalResource;

privileged aspect PersonalResource_Roo_JavaBean {
    
    public String PersonalResource.getJob() {
        return this.job;
    }
    
    public void PersonalResource.setJob(String job) {
        this.job = job;
    }
    
    public Employee PersonalResource.getEmployee() {
        return this.employee;
    }
    
    public void PersonalResource.setEmployee(Employee employee) {
        this.employee = employee;
    }
    
}
