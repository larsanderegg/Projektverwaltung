package ch.lan.teko.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResourceCollector {
	
	private Integer planedPersonalResources = 0;
    private Integer effectivPersonalResources = 0;
    
    private Integer planedFinanceResources = 0;
    private Integer effectivFinanceResources = 0;
    
	/**
	 * @return the planedPersonalResources
	 */
	public Integer getPlanedPersonalResources() {
		return planedPersonalResources;
	}
	/**
	 * @return the effectivPersonalResources
	 */
	public Integer getEffectivPersonalResources() {
		return effectivPersonalResources;
	}
	/**
	 * @return the planedFinanceResources
	 */
	public Integer getPlanedFinanceResources() {
		return planedFinanceResources;
	}
	/**
	 * @return the effectivFinanceResources
	 */
	public Integer getEffectivFinanceResources() {
		return effectivFinanceResources;
	}
	
	public void incrementPlanedFinanceResources(Integer increment){
		planedFinanceResources += increment;
	}
	
	public void incrementEffectivFinanceResources(Integer increment){
		effectivFinanceResources += increment;
	}
	
	public void incrementPlanedPersonalResources(Integer increment){
		planedPersonalResources += increment;
	}
	
	public void incrementEffectivPersonalResources(Integer increment){
		effectivPersonalResources += increment;
	}
	
	public void increment(ResourceCollector collector){
		incrementPlanedFinanceResources(collector.getPlanedFinanceResources());
		incrementEffectivFinanceResources(collector.getEffectivFinanceResources());
		
		incrementPlanedPersonalResources(collector.getPlanedPersonalResources());
		incrementEffectivPersonalResources(collector.getEffectivPersonalResources());
	}
	
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
