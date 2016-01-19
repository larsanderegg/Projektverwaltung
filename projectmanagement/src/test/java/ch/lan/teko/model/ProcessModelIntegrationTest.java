package ch.lan.teko.model;
import ch.lan.teko.repository.ProcessModelRepository;
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
@RooIntegrationTest(entity = ProcessModel.class)
public class ProcessModelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ProcessModelDataOnDemand dod;

	@Autowired
    ProcessModelRepository processModelRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = processModelRepository.count();
        Assert.assertTrue("Counter for 'ProcessModel' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = processModelRepository.findOne(id);
        Assert.assertNotNull("Find method for 'ProcessModel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProcessModel' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = processModelRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProcessModel', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProcessModel> result = processModelRepository.findAll();
        Assert.assertNotNull("Find all method for 'ProcessModel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProcessModel' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        long count = processModelRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProcessModel> result = processModelRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'ProcessModel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProcessModel' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = processModelRepository.findOne(id);
        Assert.assertNotNull("Find method for 'ProcessModel' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProcessModel(obj);
        Integer currentVersion = obj.getVersion();
        processModelRepository.flush();
        Assert.assertTrue("Version for 'ProcessModel' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = processModelRepository.findOne(id);
        boolean modified =  dod.modifyProcessModel(obj);
        Integer currentVersion = obj.getVersion();
        ProcessModel merged = processModelRepository.save(obj);
        processModelRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProcessModel' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", dod.getRandomProcessModel());
        ProcessModel obj = dod.getNewTransientProcessModel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProcessModel' identifier to be null", obj.getId());
        try {
            processModelRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        processModelRepository.flush();
        Assert.assertNotNull("Expected 'ProcessModel' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDelete() {
        ProcessModel obj = dod.getRandomProcessModel();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessModel' failed to provide an identifier", id);
        obj = processModelRepository.findOne(id);
        processModelRepository.delete(obj);
        processModelRepository.flush();
        Assert.assertNull("Failed to remove 'ProcessModel' with identifier '" + id + "'", processModelRepository.findOne(id));
    }
}
