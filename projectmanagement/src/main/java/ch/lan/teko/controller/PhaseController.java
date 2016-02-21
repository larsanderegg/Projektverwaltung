package ch.lan.teko.controller;
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

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Milestone;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Project;
import ch.lan.teko.util.URLHelper;


/**
 * Controller for an {@link Phase}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/phases")
@Controller
@GvNIXWebJQuery
public class PhaseController {
	
	/**
	 * Gets the data to show a single {@link Phase}
	 * @param id the id of the {@link Phase}
	 * @param projectId the parent project id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", params = { "projectId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id, @RequestParam(value = "projectId", required = true) Long projectId,
			Model uiModel) {
		uiModel.addAttribute("phase", Phase.findPhase(id));
		uiModel.addAttribute("itemId", id);

		Project project = Project.findProject(projectId);
		if (project != null) {
			uiModel.addAttribute("project", project);
		}

		return "phases/show";
	}

	/**
	 * Merges the given {@link Phase}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Phase}
	 * will be shown.
	 * 
	 * @param phase the {@link Phase} to merge
	 * @param bindingResult the binding results from building the given {@link Phase}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Phase phase, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, phase);
			return "phases/update";
		}
		uiModel.asMap().clear();
		phase.merge();
		return "redirect:/phases/" + URLHelper.encodeUrlPathSegment(phase.getId().toString(), httpServletRequest)
				+ "?projectId=" + URLHelper.encodeUrlPathSegment(phase.getProjectId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Phase} to update
	 * @param projectId the parent project id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = { "form", "projectId" }, produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "projectId", required = true) Long projectId, Model uiModel) {
		Phase phase = Phase.findPhase(id);
		phase.setProjectId(projectId);
		populateEditForm(uiModel, phase);

		Project project = Project.findProject(projectId);
		if (project != null) {
			uiModel.addAttribute("project", project);
		}

		return "phases/update";
	}

	private void populateEditForm(Model uiModel, Phase phase) {
		uiModel.addAttribute("phase", phase);
		uiModel.addAttribute("activitys", Activity.findAllActivitys());
		uiModel.addAttribute("milestones", Milestone.findAllMilestones());
		uiModel.addAttribute("documentreferences", DocumentReference.findAllDocumentReferences());
	}
}
