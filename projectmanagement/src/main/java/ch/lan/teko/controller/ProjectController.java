package ch.lan.teko.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ch.lan.teko.model.Employee;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.model.Project;
import ch.lan.teko.util.URLHelper;


/**
 * Controller for an {@link Project}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 */
@RequestMapping("/projects")
@Controller
@GvNIXWebJQuery
public class ProjectController {
	
	/**
	 * Persist the given {@link Project}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Project}
	 * will be shown.
	 * 
	 * @param project the {@link Project} to persist
	 * @param bindingResult the binding results from building the given {@link Project}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Project project, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/create";
		}

		uiModel.asMap().clear();
		project.persist();
		return "redirect:/projects/" + URLHelper.encodeUrlPathSegment(project.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the create form view.
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Project());
		List<String[]> dependencies = new ArrayList<String[]>();
		if (Employee.countEmployees() == 0) {
			dependencies.add(new String[] { "projectmanager", "employees" });
		}
		if (ProcessModel.countProcessModels() == 0) {
			dependencies.add(new String[] { "processModel", "processmodels" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "projects/create";
	}

	/**
	 * Gets the data to show a single {@link Project}
	 * @param id the id of the {@link Project}
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", Project.findProject(id));
		uiModel.addAttribute("itemId", id);
		return "projects/show";
	}

	/**
	 * Gets the data to show a list of {@link Project}
	 * @param page in case of paging, the page index
	 * @param size in case of paging, the size of show projects
	 * @param sortFieldName in case of sorting, the field name which should be sorted
	 * @param sortOrder in case of sorting, the sort order
	 * @param uiModel the model of the list view
	 * @return the name of the list view
	 */
	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("projects", Project.findProjectEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) Project.countProjects() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("projects", Project.findAllProjects(sortFieldName, sortOrder));
		}
		return "projects/list";
	}

	/**
	 * Merges the given {@link Project}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Project}
	 * will be shown.
	 * 
	 * @param project the {@link Project} to merge
	 * @param bindingResult the binding results from building the given {@link Project}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Project project, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/update";
		}
		uiModel.asMap().clear();
		project.merge();
		return "redirect:/projects/" + URLHelper.encodeUrlPathSegment(project.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Project} to update
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Project.findProject(id));
		return "projects/update";
	}

	/**
	 * Deletes the {@link Project} with the given id. 
	 * @param id the id of the {@link Project} to delete 
	 * @param uiModel the model of the new view
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, Model uiModel) {
		Project project = Project.findProject(id);
		project.remove();
		uiModel.asMap().clear();
		return "redirect:/projects";
	}

	private void populateEditForm(Model uiModel, Project project) {
		uiModel.addAttribute("project", project);
		uiModel.addAttribute("employees", Employee.findAllEmployees());
		uiModel.addAttribute("phases", Phase.findAllPhases());
		uiModel.addAttribute("processmodels", ProcessModel.findAllProcessModels());
	}
}
