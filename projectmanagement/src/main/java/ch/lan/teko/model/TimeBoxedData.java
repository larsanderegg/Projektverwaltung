package ch.lan.teko.model;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

public class TimeBoxedData {

	
	@DateTimeFormat(style = "M-")
    private LocalDate startDate;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate endDate;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate planedStartDate;

    /**
     */
    @DateTimeFormat(style = "M-")
    private LocalDate planedEndDate;
    
    public TimeBoxedData() {}
    
    /**
     * @param startDate
     * @param endDate
     * @param planedStartDate
     * @param planedEndDate
     */
    public TimeBoxedData(LocalDate startDate, LocalDate endDate, LocalDate planedStartDate, LocalDate planedEndDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.planedStartDate = planedStartDate;
		this.planedEndDate = planedEndDate;
	}

	public void add(ITimeBoxed timeBoxed) {
    	LocalDate otherStartDate = timeBoxed.getTimeBoxedData().getStartDate();
		if(otherStartDate != null && (startDate == null || startDate.isAfter(otherStartDate))){
    		startDate = otherStartDate;
    	}
    	
    	LocalDate otherEndDate = timeBoxed.getTimeBoxedData().getEndDate();
		if(otherEndDate != null && (endDate == null || endDate.isBefore(otherEndDate))){
    		endDate = otherEndDate;
    	}
    	
    	LocalDate otherPlanedStartDate = timeBoxed.getTimeBoxedData().getPlanedStartDate();
		if(otherPlanedStartDate != null && (planedStartDate == null || planedStartDate.isAfter(otherPlanedStartDate))){
    		planedStartDate = otherPlanedStartDate;
    	}
    	
    	LocalDate otherPlanedEndDate = timeBoxed.getTimeBoxedData().getPlanedEndDate();
		if(otherPlanedEndDate != null && (planedEndDate == null || planedEndDate.isBefore(otherPlanedEndDate))){
    		planedEndDate = otherPlanedEndDate;
    	}
    }

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @return the planedStartDate
	 */
	public LocalDate getPlanedStartDate() {
		return planedStartDate;
	}

	/**
	 * @return the planedEndDate
	 */
	public LocalDate getPlanedEndDate() {
		return planedEndDate;
	}
	
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}