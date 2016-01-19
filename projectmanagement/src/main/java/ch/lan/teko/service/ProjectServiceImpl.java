package ch.lan.teko.service;

import ch.lan.teko.model.Project;
import ch.lan.teko.repository.ProjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
    ProjectRepository projectRepository;

	public long countAllProjects() {
        return projectRepository.count();
    }

	public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

	public Project findProject(Long id) {
        return projectRepository.findOne(id);
    }

	public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

	public List<Project> findProjectEntries(int firstResult, int maxResults) {
        return projectRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveProject(Project project) {
        projectRepository.save(project);
    }

	public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
}
