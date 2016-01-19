package ch.lan.teko.model;
import ch.lan.teko.repository.ActivityRepository;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
@RooIntegrationTest(entity = Activity.class)
public class ActivityIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ActivityDataOnDemand dod;

	@Autowired
    ActivityRepository activityRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityRepository.count();
        Assert.assertTrue("Counter for 'Activity' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Activity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Activity' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Activity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Activity> result = activityRepository.findAll();
        Assert.assertNotNull("Find all method for 'Activity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Activity' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        long count = activityRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Activity> result = activityRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Activity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Activity' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Activity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyActivity(obj);
        Integer currentVersion = obj.getVersion();
        activityRepository.flush();
        Assert.assertTrue("Version for 'Activity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityRepository.findOne(id);
        boolean modified =  dod.modifyActivity(obj);
        Integer currentVersion = obj.getVersion();
        Activity merged = activityRepository.save(obj);
        activityRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Activity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", dod.getRandomActivity());
        Activity obj = dod.getNewTransientActivity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Activity' identifier to be null", obj.getId());
        try {
            activityRepository.save(obj);
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
    public void testDelete() {
        Activity obj = dod.getRandomActivity();
        Assert.assertNotNull("Data on demand for 'Activity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Activity' failed to provide an identifier", id);
        obj = activityRepository.findOne(id);
        activityRepository.delete(obj);
        activityRepository.flush();
        Assert.assertNull("Failed to remove 'Activity' with identifier '" + id + "'", activityRepository.findOne(id));
    }
}
