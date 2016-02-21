package ch.lan.teko.model;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class ActivityIntegrationTest {

    private ActivityDataOnDemand dod = new ActivityDataOnDemand();
    
    @Test
    public void testCountActivitys() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = Activity.countActivitys();
        Assert.assertTrue("Counter for 'Activity' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindActivity() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = Activity.findActivity(id);
        Assert.assertNotNull("Find method for 'Activity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Activity' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllActivitys() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = Activity.countActivitys();
        Assert.assertTrue("Too expensive to perform a find all test for 'Activity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Activity> result = Activity.findAllActivitys();
        Assert.assertNotNull("Find all method for 'Activity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Activity' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindActivityEntries() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = Activity.countActivitys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Activity> result = Activity.findActivityEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Activity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Activity' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = Activity.findActivity(id);
        Assert.assertNotNull("Find method for 'Activity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyActivity(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Activity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = Activity.findActivity(id);
        boolean modified =  dod.modifyActivity(obj);
        Integer currentVersion = obj.getVersion();
        Activity merged = (Activity)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Activity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        Activity obj = dod.getNewTransientActivity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Activity' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Activity' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = Activity.findActivity(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Activity' with identifier '" + id + "'", Activity.findActivity(id));
    }
}
