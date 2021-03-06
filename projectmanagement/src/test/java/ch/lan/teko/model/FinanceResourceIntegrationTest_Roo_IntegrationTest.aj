// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.model.FinanceResourceDataOnDemand;
import ch.lan.teko.model.FinanceResourceIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FinanceResourceIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FinanceResourceIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FinanceResourceIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FinanceResourceIntegrationTest: @Transactional;
    
    @Autowired
    FinanceResourceDataOnDemand FinanceResourceIntegrationTest.dod;
    
    @Test
    public void FinanceResourceIntegrationTest.testCountFinanceResources() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = FinanceResource.countFinanceResources();
        Assert.assertTrue("Counter for 'FinanceResource' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testFindFinanceResource() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = FinanceResource.findFinanceResource(id);
        Assert.assertNotNull("Find method for 'FinanceResource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FinanceResource' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testFindAllFinanceResources() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = FinanceResource.countFinanceResources();
        Assert.assertTrue("Too expensive to perform a find all test for 'FinanceResource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FinanceResource> result = FinanceResource.findAllFinanceResources();
        Assert.assertNotNull("Find all method for 'FinanceResource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FinanceResource' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testFindFinanceResourceEntries() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = FinanceResource.countFinanceResources();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FinanceResource> result = FinanceResource.findFinanceResourceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'FinanceResource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FinanceResource' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testFlush() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = FinanceResource.findFinanceResource(id);
        Assert.assertNotNull("Find method for 'FinanceResource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFinanceResource(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'FinanceResource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testMergeUpdate() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = FinanceResource.findFinanceResource(id);
        boolean modified =  dod.modifyFinanceResource(obj);
        Integer currentVersion = obj.getVersion();
        FinanceResource merged = (FinanceResource)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FinanceResource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        FinanceResource obj = dod.getNewTransientFinanceResource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FinanceResource' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'FinanceResource' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FinanceResourceIntegrationTest.testRemove() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = FinanceResource.findFinanceResource(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'FinanceResource' with identifier '" + id + "'", FinanceResource.findFinanceResource(id));
    }
    
}
