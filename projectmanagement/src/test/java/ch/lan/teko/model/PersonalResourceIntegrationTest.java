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
public class PersonalResourceIntegrationTest {

    private PersonalResourceDataOnDemand dod = new PersonalResourceDataOnDemand();
    
    @Test
    public void testCountPersonalResources() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = PersonalResource.countPersonalResources();
        Assert.assertTrue("Counter for 'PersonalResource' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindPersonalResource() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = PersonalResource.findPersonalResource(id);
        Assert.assertNotNull("Find method for 'PersonalResource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'PersonalResource' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllPersonalResources() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = PersonalResource.countPersonalResources();
        Assert.assertTrue("Too expensive to perform a find all test for 'PersonalResource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<PersonalResource> result = PersonalResource.findAllPersonalResources();
        Assert.assertNotNull("Find all method for 'PersonalResource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'PersonalResource' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindPersonalResourceEntries() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        long count = PersonalResource.countPersonalResources();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<PersonalResource> result = PersonalResource.findPersonalResourceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'PersonalResource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'PersonalResource' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = PersonalResource.findPersonalResource(id);
        Assert.assertNotNull("Find method for 'PersonalResource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPersonalResource(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'PersonalResource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = PersonalResource.findPersonalResource(id);
        boolean modified =  dod.modifyPersonalResource(obj);
        Integer currentVersion = obj.getVersion();
        PersonalResource merged = (PersonalResource)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'PersonalResource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", dod.getRandomPersonalResource());
        PersonalResource obj = dod.getNewTransientPersonalResource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'PersonalResource' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'PersonalResource' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        PersonalResource obj = dod.getRandomPersonalResource();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'PersonalResource' failed to provide an identifier", id);
        obj = PersonalResource.findPersonalResource(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'PersonalResource' with identifier '" + id + "'", PersonalResource.findPersonalResource(id));
    }
}
