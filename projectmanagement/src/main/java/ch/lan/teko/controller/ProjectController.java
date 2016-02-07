package ch.lan.teko.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ch.lan.teko.model.Project;
import ch.lan.teko.service.EmployeeService;
import ch.lan.teko.service.PhaseService;
import ch.lan.teko.service.ProcessModelService;
import ch.lan.teko.service.ProjectService;
import ch.lan.teko.util.URLHelper;

@RequestMapping("/projects")
@Controller
@RooWebScaffold(path = "projects", formBackingObject = Project.class)
public class ProjectController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ProcessModelService processModelService;

	@Autowired
	ProjectService projectService;

	@Autowired
	PhaseService phaseService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Project project, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/create";
		}

		uiModel.asMap().clear();
		projectService.saveProject(project);
		return "redirect:/projects/" + URLHelper.encodeUrlPathSegment(project.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Project());
		List<String[]> dependencies = new ArrayList<String[]>();
		if (employeeService.countAllEmployees() == 0) {
			dependencies.add(new String[] { "projectmanager", "employees" });
		}
		if (processModelService.countAllProcessModels() == 0) {
			dependencies.add(new String[] { "processModel", "processmodels" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "projects/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", projectService.findProject(id));
		uiModel.addAttribute("itemId", id);
		return "projects/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("projects", Project.findProjectEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) projectService.countAllProjects() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("projects", Project.findAllProjects(sortFieldName, sortOrder));
		}
		return "projects/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Project project, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/update";
		}
		uiModel.asMap().clear();
		projectService.updateProject(project);
		return "redirect:/projects/" + URLHelper.encodeUrlPathSegment(project.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, projectService.findProject(id));
		return "projects/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Project project = projectService.findProject(id);
		projectService.deleteProject(project);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/projects";
	}

	void populateEditForm(Model uiModel, Project project) {
		uiModel.addAttribute("project", project);
		uiModel.addAttribute("employees", employeeService.findAllEmployees());
		uiModel.addAttribute("phases", phaseService.findAllPhases());
		uiModel.addAttribute("processmodels", processModelService.findAllProcessModels());
	}
}
