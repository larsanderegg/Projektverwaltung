// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.model.ProcessModelDataOnDemand;
import ch.lan.teko.model.ProcessModelIntegrationTest;
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

privileged aspect ProcessModelIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ProcessModelIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ProcessModelIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ProcessModelIntegrationTest: @Transactional;
    
    @Autowired
    ProcessModelDataOnDemand ProcessModelIntegrationTest.dod;
    
    @Test
    public void ProcessModelIntegrationTest.testCountProcessModels() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = ProcessModel.countProcessModels();
        Assert.assertTrue("Counter for 'ProcessModel' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ProcessModelIntegrationTest.testFindProcessModel() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = ProcessModel.findProcessModel(id);
        Assert.assertNotNull("Find method for 'ProcessModel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProcessModel' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ProcessModelIntegrationTest.testFindAllProcessModels() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = ProcessModel.countProcessModels();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProcessModel', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProcessModel> result = ProcessModel.findAllProcessModels();
        Assert.assertNotNull("Find all method for 'ProcessModel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProcessModel' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ProcessModelIntegrationTest.testFindProcessModelEntries() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = ProcessModel.countProcessModels();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProcessModel> result = ProcessModel.findProcessModelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProcessModel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProcessModel' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ProcessModelIntegrationTest.testFlush() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = ProcessModel.findProcessModel(id);
        Assert.assertNotNull("Find method for 'ProcessModel' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProcessModel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProcessModel' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProcessModelIntegrationTest.testMergeUpdate() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = ProcessModel.findProcessModel(id);
        boolean modified =  dod.modifyProcessModel(obj);
        Integer currentVersion = obj.getVersion();
        ProcessModel merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProcessModel' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProcessModelIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        ProcessModel obj = dod.getNewTransientProcessModel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProcessModel' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ProcessModel' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ProcessModelIntegrationTest.testRemove() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = ProcessModel.findProcessModel(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProcessModel' with identifier '" + id + "'", ProcessModel.findProcessModel(id));
    }
    
}
