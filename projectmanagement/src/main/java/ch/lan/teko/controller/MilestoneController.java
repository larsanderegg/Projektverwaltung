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

import ch.lan.teko.model.Milestone;
import ch.lan.teko.model.Phase;
import ch.lan.teko.util.URLHelper;

@RequestMapping("/milestones")
@Controller
@GvNIXWebJQuery
public class MilestoneController {
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Milestone milestone, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, milestone);
			return "milestones/create";
		}
		uiModel.asMap().clear();
		milestone.persist();

		Phase phase = Phase.findPhase(milestone.getPhaseId());
		if (phase != null) {
			phase.addMilestone(milestone);
			phase.merge();
		} else {
			return "error";
		}

		return "redirect:/milestones/" + URLHelper.encodeUrlPathSegment(milestone.getId().toString(), httpServletRequest)
				+ "?phaseId=" + URLHelper.encodeUrlPathSegment(phase.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = { "form", "phaseId" }, produces = "text/html")
	public String createForm(@RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
		Milestone milestone = new Milestone();
		milestone.setPhaseId(phaseId);

		populateEditForm(uiModel, milestone);

		Phase phase = Phase.findPhase(phaseId);
		if (phase != null) {
			uiModel.addAttribute("phase", phase);
		}

		return "milestones/create";
	}

	@RequestMapping(value = "/{id}", params = { "phaseId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id, @RequestParam(value = "phaseId", required = true) Long phaseId,
			Model uiModel) {
		uiModel.addAttribute("milestone", Milestone.findMilestone(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("phase", Phase.findPhase(phaseId));
		return "milestones/show";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Milestone milestone, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, milestone);
			return "milestones/update";
		}
		uiModel.asMap().clear();
		milestone.merge();
		return "redirect:/milestones/" + URLHelper.encodeUrlPathSegment(milestone.getId().toString(), httpServletRequest)
				+ "?phaseId=" + URLHelper.encodeUrlPathSegment(milestone.getPhaseId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = { "form", "phaseId" }, produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
		Milestone milestone = Milestone.findMilestone(id);
		milestone.setPhaseId(phaseId);

		populateEditForm(uiModel, milestone);

		Phase phase = Phase.findPhase(phaseId);
		if (phase != null) {
			uiModel.addAttribute("phase", phase);
		}

		return "milestones/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Milestone milestone = Milestone.findMilestone(id);
		milestone.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/milestones";
	}

	void populateEditForm(Model uiModel, Milestone milestone) {
		uiModel.addAttribute("milestone", milestone);
	}
}
