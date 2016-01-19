// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.model;

import ch.lan.teko.model.ActivityIntegrationTest;
import ch.lan.teko.service.ActivityService;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect ActivityIntegrationTest_Roo_IntegrationTest {
    
    @Autowired
    ActivityService ActivityIntegrationTest.activityService;
    
    @Test
    public void ActivityIntegrationTest.testCountAllActivitys() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityService.countAllActivitys();
        Assert.assertTrue("Counter for 'Activity' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ActivityIntegrationTest.testFindActivity() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityService.findActivity(id);
        Assert.assertNotNull("Find method for 'Activity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Activity' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ActivityIntegrationTest.testFindAllActivitys() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityService.countAllActivitys();
        Assert.assertTrue("Too expensive to perform a find all test for 'Activity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Activity> result = activityService.findAllActivitys();
        Assert.assertNotNull("Find all method for 'Activity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Activity' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ActivityIntegrationTest.testFindActivityEntries() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityService.countAllActivitys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Activity> result = activityService.findActivityEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Activity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Activity' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ActivityIntegrationTest.testUpdateActivityUpdate() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityService.findActivity(id);
        boolean modified =  dod.modifyActivity(obj);
        Integer currentVersion = obj.getVersion();
        Activity merged = activityService.updateActivity(obj);
        activityRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Activity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ActivityIntegrationTest.testSaveActivity() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        Activity obj = dod.getNewTransientActivity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Activity' identifier to be null", obj.getId());
        try {
            activityService.saveActivity(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        activityRepository.flush();
        Assert.assertNotNull("Expected 'Activity' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ActivityIntegrationTest.testDeleteActivity() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityService.findActivity(id);
        activityService.deleteActivity(obj);
        activityRepository.flush();
        Assert.assertNull("Failed to remove 'Activity' with identifier '" + id + "'", activityService.findActivity(id));
    }
    
}
