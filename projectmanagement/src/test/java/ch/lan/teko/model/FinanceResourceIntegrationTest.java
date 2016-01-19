package ch.lan.teko.model;
import ch.lan.teko.repository.FinanceResourceRepository;
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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = FinanceResource.class)
public class FinanceResourceIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    FinanceResourceDataOnDemand dod;

	@Autowired
    FinanceResourceRepository financeResourceRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = financeResourceRepository.count();
        Assert.assertTrue("Counter for 'FinanceResource' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = financeResourceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'FinanceResource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FinanceResource' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = financeResourceRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'FinanceResource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FinanceResource> result = financeResourceRepository.findAll();
        Assert.assertNotNull("Find all method for 'FinanceResource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FinanceResource' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        long count = financeResourceRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FinanceResource> result = financeResourceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'FinanceResource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FinanceResource' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = financeResourceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'FinanceResource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFinanceResource(obj);
        Integer currentVersion = obj.getVersion();
        financeResourceRepository.flush();
        Assert.assertTrue("Version for 'FinanceResource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = financeResourceRepository.findOne(id);
        boolean modified =  dod.modifyFinanceResource(obj);
        Integer currentVersion = obj.getVersion();
        FinanceResource merged = (FinanceResource)financeResourceRepository.save(obj);
        financeResourceRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FinanceResource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", dod.getRandomFinanceResource());
        FinanceResource obj = dod.getNewTransientFinanceResource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FinanceResource' identifier to be null", obj.getId());
        try {
            financeResourceRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        financeResourceRepository.flush();
        Assert.assertNotNull("Expected 'FinanceResource' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDelete() {
        FinanceResource obj = dod.getRandomFinanceResource();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FinanceResource' failed to provide an identifier", id);
        obj = financeResourceRepository.findOne(id);
        financeResourceRepository.delete(obj);
        financeResourceRepository.flush();
        Assert.assertNull("Failed to remove 'FinanceResource' with identifier '" + id + "'", financeResourceRepository.findOne(id));
    }
}
