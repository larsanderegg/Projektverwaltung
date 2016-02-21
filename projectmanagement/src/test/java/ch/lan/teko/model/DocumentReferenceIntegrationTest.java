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
public class DocumentReferenceIntegrationTest {

    private DocumentReferenceDataOnDemand dod = new DocumentReferenceDataOnDemand();
    
    @Test
    public void testCountDocumentReferences() {
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", dod.getRandomDocumentReference());
        long count = DocumentReference.countDocumentReferences();
        Assert.assertTrue("Counter for 'DocumentReference' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindDocumentReference() {
        DocumentReference obj = dod.getRandomDocumentReference();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to provide an identifier", id);
        obj = DocumentReference.findDocumentReference(id);
        Assert.assertNotNull("Find method for 'DocumentReference' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'DocumentReference' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllDocumentReferences() {
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", dod.getRandomDocumentReference());
        long count = DocumentReference.countDocumentReferences();
        Assert.assertTrue("Too expensive to perform a find all test for 'DocumentReference', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<DocumentReference> result = DocumentReference.findAllDocumentReferences();
        Assert.assertNotNull("Find all method for 'DocumentReference' illegally returned null", result);
        Assert.assertTrue("Find all method for 'DocumentReference' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindDocumentReferenceEntries() {
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", dod.getRandomDocumentReference());
        long count = DocumentReference.countDocumentReferences();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<DocumentReference> result = DocumentReference.findDocumentReferenceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'DocumentReference' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'DocumentReference' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        DocumentReference obj = dod.getRandomDocumentReference();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to provide an identifier", id);
        obj = DocumentReference.findDocumentReference(id);
        Assert.assertNotNull("Find method for 'DocumentReference' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDocumentReference(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'DocumentReference' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        DocumentReference obj = dod.getRandomDocumentReference();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to provide an identifier", id);
        obj = DocumentReference.findDocumentReference(id);
        boolean modified =  dod.modifyDocumentReference(obj);
        Integer currentVersion = obj.getVersion();
        DocumentReference merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'DocumentReference' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", dod.getRandomDocumentReference());
        DocumentReference obj = dod.getNewTransientDocumentReference(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'DocumentReference' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'DocumentReference' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        DocumentReference obj = dod.getRandomDocumentReference();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DocumentReference' failed to provide an identifier", id);
        obj = DocumentReference.findDocumentReference(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'DocumentReference' with identifier '" + id + "'", DocumentReference.findDocumentReference(id));
    }
}
