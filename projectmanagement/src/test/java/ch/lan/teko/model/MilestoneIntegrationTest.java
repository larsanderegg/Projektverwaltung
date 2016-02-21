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
public class MilestoneIntegrationTest {

    private MilestoneDataOnDemand dod = new MilestoneDataOnDemand();
    
    @Test
    public void testCountMilestones() {
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", dod.getRandomMilestone());
        long count = Milestone.countMilestones();
        Assert.assertTrue("Counter for 'Milestone' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindMilestone() {
        Milestone obj = dod.getRandomMilestone();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to provide an identifier", id);
        obj = Milestone.findMilestone(id);
        Assert.assertNotNull("Find method for 'Milestone' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Milestone' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllMilestones() {
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", dod.getRandomMilestone());
        long count = Milestone.countMilestones();
        Assert.assertTrue("Too expensive to perform a find all test for 'Milestone', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Milestone> result = Milestone.findAllMilestones();
        Assert.assertNotNull("Find all method for 'Milestone' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Milestone' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindMilestoneEntries() {
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", dod.getRandomMilestone());
        long count = Milestone.countMilestones();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Milestone> result = Milestone.findMilestoneEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Milestone' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Milestone' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Milestone obj = dod.getRandomMilestone();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to provide an identifier", id);
        obj = Milestone.findMilestone(id);
        Assert.assertNotNull("Find method for 'Milestone' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMilestone(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Milestone' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        Milestone obj = dod.getRandomMilestone();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to provide an identifier", id);
        obj = Milestone.findMilestone(id);
        boolean modified =  dod.modifyMilestone(obj);
        Integer currentVersion = obj.getVersion();
        Milestone merged = (Milestone)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Milestone' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", dod.getRandomMilestone());
        Milestone obj = dod.getNewTransientMilestone(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Milestone' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Milestone' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Milestone' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        Milestone obj = dod.getRandomMilestone();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Milestone' failed to provide an identifier", id);
        obj = Milestone.findMilestone(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Milestone' with identifier '" + id + "'", Milestone.findMilestone(id));
    }
}
