// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Employee;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Employee_Roo_Jpa_Entity {
    
    declare @type: Employee: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Employee.id;
    
    @Version
    @Column(name = "version")
    private Integer Employee.version;
    
    public Long Employee.getId() {
        return this.id;
    }
    
    public void Employee.setId(Long id) {
        this.id = id;
    }
    
    public Integer Employee.getVersion() {
        return this.version;
    }
    
    public void Employee.setVersion(Integer version) {
        this.version = version;
    }
    
}
