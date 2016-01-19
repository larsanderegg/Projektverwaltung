package ch.lan.teko.model;
import ch.lan.teko.repository.ProjectRepository;
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
@RooIntegrationTest(entity = Project.class)
public class ProjectIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ProjectDataOnDemand dod;

	@Autowired
    ProjectRepository projectRepository;

	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", dod.getRandomProject());
        long count = projectRepository.count();
        Assert.assertTrue("Counter for 'Project' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFind() {
        Project obj = dod.getRandomProject();
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Project' failed to provide an identifier", id);
        obj = projectRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Project' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Project' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", dod.getRandomProject());
        long count = projectRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Project', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Project> result = projectRepository.findAll();
        Assert.assertNotNull("Find all method for 'Project' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Project' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", dod.getRandomProject());
        long count = projectRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Project> result = projectRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Project' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Project' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Project obj = dod.getRandomProject();
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Project' failed to provide an identifier", id);
        obj = projectRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Project' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProject(obj);
        Integer currentVersion = obj.getVersion();
        projectRepository.flush();
        Assert.assertTrue("Version for 'Project' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUpdate() {
        Project obj = dod.getRandomProject();
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Project' failed to provide an identifier", id);
        obj = projectRepository.findOne(id);
        boolean modified =  dod.modifyProject(obj);
        Integer currentVersion = obj.getVersion();
        Project merged = projectRepository.save(obj);
        projectRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Project' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", dod.getRandomProject());
        Project obj = dod.getNewTransientProject(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Project' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Project' identifier to be null", obj.getId());
        try {
            projectRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        projectRepository.flush();
        Assert.assertNotNull("Expected 'Project' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDelete() {
        Project obj = dod.getRandomProject();
        Assert.assertNotNull("Data on demand for 'Project' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Project' failed to provide an identifier", id);
        obj = projectRepository.findOne(id);
        projectRepository.delete(obj);
        projectRepository.flush();
        Assert.assertNull("Failed to remove 'Project' with identifier '" + id + "'", projectRepository.findOne(id));
    }
}
