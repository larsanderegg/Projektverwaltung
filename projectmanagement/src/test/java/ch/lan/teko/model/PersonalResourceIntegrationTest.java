package ch.lan.teko.model;
import ch.lan.teko.repository.PersonalResourceRepository;
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
@RooIntegrationTest(entity = PersonalResource.class)
public class PersonalResourceIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    PersonalResourceDataOnDemand dod;

	@Autowired
    PersonalResourceRepository personalResourceRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = personalResourceRepository.count();
        Assert.assertTrue("Counter for 'PersonalResource' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = personalResourceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'PersonalResource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'PersonalResource' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = personalResourceRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'PersonalResource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<PersonalResource> result = personalResourceRepository.findAll();
        Assert.assertNotNull("Find all method for 'PersonalResource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'PersonalResource' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = personalResourceRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<PersonalResource> result = personalResourceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'PersonalResource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'PersonalResource' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = personalResourceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'PersonalResource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPersonalResource(obj);
        Integer currentVersion = obj.getVersion();
        personalResourceRepository.flush();
        Assert.assertTrue("Version for 'PersonalResource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = personalResourceRepository.findOne(id);
        boolean modified =  dod.modifyPersonalResource(obj);
        Integer currentVersion = obj.getVersion();
        PersonalResource merged = (PersonalResource)personalResourceRepository.save(obj);
        personalResourceRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'PersonalResource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        PersonalResource obj = dod.getNewTransientPersonalResource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'PersonalResource' identifier to be null", obj.getId());
        try {
            personalResourceRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        personalResourceRepository.flush();
        Assert.assertNotNull("Expected 'PersonalResource' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDelete() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = personalResourceRepository.findOne(id);
        personalResourceRepository.delete(obj);
        personalResourceRepository.flush();
        Assert.assertNull("Failed to remove 'PersonalResource' with identifier '" + id + "'", personalResourceRepository.findOne(id));
    }
}
