package ch.lan.teko.model;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Calculates the planed and effective duration.
 * @author landeregg
 */
public class TimeBoxedData {

	
	@DateTimeFormat(style = "M-")
    private LocalDate startDate;

    @DateTimeFormat(style = "M-")
    private LocalDate endDate;

    @DateTimeFormat(style = "M-")
    private LocalDate planedStartDate;
    
    @DateTimeFormat(style = "M-")
    private LocalDate planedEndDate;
    
    /**
     * Default C'tor
     */
    public TimeBoxedData() {}
    
    /**
     * C'tor with planed and effective values
     * @param startDate the effective start date
     * @param endDate the effective end date
     * @param planedStartDate the planed start date
     * @param planedEndDate the planed end date
     */
    public TimeBoxedData(LocalDate startDate, LocalDate endDate, LocalDate planedStartDate, LocalDate planedEndDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.planedStartDate = planedStartDate;
		this.planedEndDate = planedEndDate;
	}

	/**
	 * Adds the given {@link ITimeBoxed} to calculate new durations
	 * @param timeBoxed the values to add
	 */
	public void add(ITimeBoxed timeBoxed) {
    	TimeBoxedData timeBoxedData = timeBoxed.getTimeBoxedData();
		LocalDate otherStartDate = timeBoxedData.getStartDate();
		if(otherStartDate != null && (startDate == null || startDate.isAfter(otherStartDate))){
    		startDate = otherStartDate;
    	}
    	
    	LocalDate otherEndDate = timeBoxedData.getEndDate();
		if(otherEndDate != null && (endDate == null || endDate.isBefore(otherEndDate))){
    		endDate = otherEndDate;
    	}
    	
    	LocalDate otherPlanedStartDate = timeBoxedData.getPlanedStartDate();
		if(otherPlanedStartDate != null && (planedStartDate == null || planedStartDate.isAfter(otherPlanedStartDate))){
    		planedStartDate = otherPlanedStartDate;
    	}
    	
    	LocalDate otherPlanedEndDate = timeBoxedData.getPlanedEndDate();
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
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}