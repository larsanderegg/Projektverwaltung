package ch.lan.teko.model;
import ch.lan.teko.repository.EmployeeRepository;
import ch.lan.teko.service.EmployeeService;
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
@RooIntegrationTest(entity = Employee.class)
public class EmployeeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    EmployeeDataOnDemand dod;

	@Autowired
    EmployeeService employeeService;

	@Autowired
    EmployeeRepository employeeRepository;

	@Test
    public void testCountAllEmployees() {
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", dod.getRandomEmployee());
        long count = employeeService.countAllEmployees();
        Assert.assertTrue("Counter for 'Employee' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindEmployee() {
        Employee obj = dod.getRandomEmployee();
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Employee' failed to provide an identifier", id);
        obj = employeeService.findEmployee(id);
        Assert.assertNotNull("Find method for 'Employee' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Employee' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllEmployees() {
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", dod.getRandomEmployee());
        long count = employeeService.countAllEmployees();
        Assert.assertTrue("Too expensive to perform a find all test for 'Employee', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Employee> result = employeeService.findAllEmployees();
        Assert.assertNotNull("Find all method for 'Employee' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Employee' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindEmployeeEntries() {
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", dod.getRandomEmployee());
        long count = employeeService.countAllEmployees();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Employee> result = employeeService.findEmployeeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Employee' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Employee' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Employee obj = dod.getRandomEmployee();
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Employee' failed to provide an identifier", id);
        obj = employeeService.findEmployee(id);
        Assert.assertNotNull("Find method for 'Employee' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyEmployee(obj);
        Integer currentVersion = obj.getVersion();
        employeeRepository.flush();
        Assert.assertTrue("Version for 'Employee' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateEmployeeUpdate() {
        Employee obj = dod.getRandomEmployee();
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Employee' failed to provide an identifier", id);
        obj = employeeService.findEmployee(id);
        boolean modified =  dod.modifyEmployee(obj);
        Integer currentVersion = obj.getVersion();
        Employee merged = employeeService.updateEmployee(obj);
        employeeRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Employee' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveEmployee() {
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", dod.getRandomEmployee());
        Employee obj = dod.getNewTransientEmployee(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Employee' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Employee' identifier to be null", obj.getId());
        try {
            employeeService.saveEmployee(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        employeeRepository.flush();
        Assert.assertNotNull("Expected 'Employee' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteEmployee() {
        Employee obj = dod.getRandomEmployee();
        Assert.assertNotNull("Data on demand for 'Employee' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Employee' failed to provide an identifier", id);
        obj = employeeService.findEmployee(id);
        employeeService.deleteEmployee(obj);
        employeeRepository.flush();
        Assert.assertNull("Failed to remove 'Employee' with identifier '" + id + "'", employeeService.findEmployee(id));
    }
}
