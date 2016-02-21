package ch.lan.teko.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Collects planed and effective resources.
 * @author landeregg
 */
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
	
	/**
	 * Increments the planed finance resource with the given number.
	 * @param increment value to increment
	 */
	public void incrementPlanedFinanceResources(Integer increment){
		planedFinanceResources += increment;
	}
	
	
	/**
	 * Increments the effective finance resource with the given number.
	 * @param increment value to increment
	 */
	public void incrementEffectivFinanceResources(Integer increment){
		effectivFinanceResources += increment;
	}
	
	/**
	 * Increments the planed personal resource with the given number.
	 * @param increment value to increment
	 */
	public void incrementPlanedPersonalResources(Integer increment){
		planedPersonalResources += increment;
	}
	
	/**
	 * Increments the effective personal resource with the given number.
	 * @param increment value to increment
	 */
	public void incrementEffectivPersonalResources(Integer increment){
		effectivPersonalResources += increment;
	}
	
	/**
	 * Increments all resources from the given {@link ResourceCollector}
	 * @param collector values to increment
	 */
	public void increment(ResourceCollector collector){
		incrementPlanedFinanceResources(collector.getPlanedFinanceResources());
		incrementEffectivFinanceResources(collector.getEffectivFinanceResources());
		
		incrementPlanedPersonalResources(collector.getPlanedPersonalResources());
		incrementEffectivPersonalResources(collector.getEffectivPersonalResources());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
