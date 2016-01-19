package ch.lan.teko.model;
import ch.lan.teko.repository.ProjectRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = Project.class)
public class ProjectDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Project> data;

	@Autowired
    ProjectRepository projectRepository;

	public Project getNewTransientProject(int index) {
        Project obj = new Project();
        return obj;
    }

	public Project getSpecificProject(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Project obj = data.get(index);
        Long id = obj.getId();
        return projectRepository.findOne(id);
    }

	public Project getRandomProject() {
        init();
        Project obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return projectRepository.findOne(id);
    }

	public boolean modifyProject(Project obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = projectRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Project' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Project>();
        for (int i = 0; i < 10; i++) {
            Project obj = getNewTransientProject(i);
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
            data.add(obj);
        }
    }
}
