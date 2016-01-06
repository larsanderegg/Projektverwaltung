// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Phase;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

privileged aspect Phase_Roo_JavaBean {
    
    public Set<DocumentReference> Phase.getLinks() {
        return this.links;
    }
    
    public void Phase.setLinks(Set<DocumentReference> links) {
        this.links = links;
    }
    
    public LocalDate Phase.getReviewDate() {
        return this.reviewDate;
    }
    
    public void Phase.setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public LocalDate Phase.getApprovalDate() {
        return this.approvalDate;
    }
    
    public void Phase.setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public LocalDate Phase.getPlanedReviewDate() {
        return this.planedReviewDate;
    }
    
    public void Phase.setPlanedReviewDate(LocalDate planedReviewDate) {
        this.planedReviewDate = planedReviewDate;
    }
    
    public Byte Phase.getProgress() {
        return this.progress;
    }
    
    public void Phase.setProgress(Byte progress) {
        this.progress = progress;
    }
    
    public String Phase.getPhaseState() {
        return this.phaseState;
    }
    
    public void Phase.setPhaseState(String phaseState) {
        this.phaseState = phaseState;
    }
    
    public List<Activity> Phase.getActivities() {
        return this.activities;
    }
    
    public void Phase.setActivities(List<Activity> activities) {
        this.activities = activities;
    }
    
}
