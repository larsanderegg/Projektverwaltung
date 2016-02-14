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

@RequestMapping("/phases")
@Controller
@GvNIXWebJQuery
public class PhaseController {
	
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

	void populateEditForm(Model uiModel, Phase phase) {
		uiModel.addAttribute("phase", phase);
		uiModel.addAttribute("activitys", Activity.findAllActivitys());
		uiModel.addAttribute("milestones", Milestone.findAllMilestones());
		uiModel.addAttribute("documentreferences", DocumentReference.findAllDocumentReferences());
	}
}
