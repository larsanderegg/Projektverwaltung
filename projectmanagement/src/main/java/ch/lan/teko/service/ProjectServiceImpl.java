package ch.lan.teko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Project;
import ch.lan.teko.repository.ProjectRepository;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
    ProjectRepository projectRepository;
	
	@Autowired
    PhaseService phaseService;

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
		
		List<Phase> phases = phaseService.generatePhases(project.getProcessModel());
        project.setPhases(phases);
		
        projectRepository.save(project);
    }

	public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

	@Override
	public void addDocumentReference(Long projectId, DocumentReference documentReference) {
//		Project project = findProject(projectId);
//		if(project != null){
//			project.getLinks().add(documentReference);
//			updateProject(project);
//		}
		System.out.println("not implemented");
	}
}
