package ch.lan.teko.service;
import ch.lan.teko.model.Project;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.Project.class })
public interface ProjectService {

	public abstract long countAllProjects();


	public abstract void deleteProject(Project project);


	public abstract Project findProject(Long id);


	public abstract List<Project> findAllProjects();


	public abstract List<Project> findProjectEntries(int firstResult, int maxResults);


	public abstract void saveProject(Project project);


	public abstract Project updateProject(Project project);

}
