package ch.lan.teko.model;
import ch.lan.teko.repository.PhaseRepository;
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
@RooIntegrationTest(entity = Phase.class)
public class PhaseIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    PhaseDataOnDemand dod;

	@Autowired
    PhaseRepository phaseRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", dod.getRandomPhase());
        long count = phaseRepository.count();
        Assert.assertTrue("Counter for 'Phase' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        Phase obj = dod.getRandomPhase();
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Phase' failed to provide an identifier", id);
        obj = phaseRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Phase' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Phase' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", dod.getRandomPhase());
        long count = phaseRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Phase', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Phase> result = phaseRepository.findAll();
        Assert.assertNotNull("Find all method for 'Phase' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Phase' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", dod.getRandomPhase());
        long count = phaseRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Phase> result = phaseRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Phase' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Phase' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Phase obj = dod.getRandomPhase();
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Phase' failed to provide an identifier", id);
        obj = phaseRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Phase' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPhase(obj);
        Integer currentVersion = obj.getVersion();
        phaseRepository.flush();
        Assert.assertTrue("Version for 'Phase' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        Phase obj = dod.getRandomPhase();
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Phase' failed to provide an identifier", id);
        obj = phaseRepository.findOne(id);
        boolean modified =  dod.modifyPhase(obj);
        Integer currentVersion = obj.getVersion();
        Phase merged = phaseRepository.save(obj);
        phaseRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Phase' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", dod.getRandomPhase());
        Phase obj = dod.getNewTransientPhase(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Phase' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Phase' identifier to be null", obj.getId());
        try {
            phaseRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        phaseRepository.flush();
        Assert.assertNotNull("Expected 'Phase' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDelete() {
        Phase obj = dod.getRandomPhase();
        Assert.assertNotNull("Data on demand for 'Phase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Phase' failed to provide an identifier", id);
        obj = phaseRepository.findOne(id);
        phaseRepository.delete(obj);
        phaseRepository.flush();
        Assert.assertNull("Failed to remove 'Phase' with identifier '" + id + "'", phaseRepository.findOne(id));
    }
}
