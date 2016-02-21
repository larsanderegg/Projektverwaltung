package ch.lan.teko.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class ResourceCollectorTest {

	@Test
	public void singleIncrement() {
		ResourceCollector rc = new ResourceCollector();
		rc.incrementEffectivFinanceResources(10);
		rc.incrementEffectivFinanceResources(10);
		
		rc.incrementEffectivPersonalResources(100);
		rc.incrementEffectivPersonalResources(100);
		
		rc.incrementPlanedFinanceResources(1000);
		rc.incrementPlanedFinanceResources(1000);
		
		rc.incrementPlanedPersonalResources(10000);
		rc.incrementPlanedPersonalResources(10000);
		
		assertEquals(new Integer(20), rc.getEffectivFinanceResources());
		assertEquals(new Integer(200), rc.getEffectivPersonalResources());
		assertEquals(new Integer(2000), rc.getPlanedFinanceResources());
		assertEquals(new Integer(20000), rc.getPlanedPersonalResources());
	}
	
	@Test
	public void incrementFromCollector() {
		ResourceCollector rc1 = new ResourceCollector();
		rc1.incrementEffectivFinanceResources(10);
		rc1.incrementEffectivPersonalResources(100);
		rc1.incrementPlanedFinanceResources(1000);
		rc1.incrementPlanedPersonalResources(10000);
		
		ResourceCollector rc2 = new ResourceCollector();
		rc2.incrementEffectivFinanceResources(10);
		rc2.incrementEffectivPersonalResources(100);
		rc2.incrementPlanedFinanceResources(1000);
		rc2.incrementPlanedPersonalResources(10000);
		
		ResourceCollector rc3 = new ResourceCollector();
		rc3.increment(rc1);
		rc3.increment(rc2);
		
		assertEquals(new Integer(20), rc3.getEffectivFinanceResources());
		assertEquals(new Integer(200), rc3.getEffectivPersonalResources());
		assertEquals(new Integer(2000), rc3.getPlanedFinanceResources());
		assertEquals(new Integer(20000), rc3.getPlanedPersonalResources());
	}

}
