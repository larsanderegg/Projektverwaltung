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

/**
 * Controller for an {@link Milestone}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/milestones")
@Controller
@GvNIXWebJQuery
public class MilestoneController {
	
	/**
	 * Persist the given {@link Milestone}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Milestone}
	 * will be shown.
	 * 
	 * @param milestone the {@link Milestone} to persist
	 * @param bindingResult the binding results from building the given {@link Milestone}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
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

	/**
	 * Gets the data for the create form view.
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
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

	/**
	 * Gets the data to show a single {@link Milestone}
	 * @param id the id of the {@link Milestone}
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", params = { "phaseId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id, @RequestParam(value = "phaseId", required = true) Long phaseId,
			Model uiModel) {
		uiModel.addAttribute("milestone", Milestone.findMilestone(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("phase", Phase.findPhase(phaseId));
		return "milestones/show";
	}

	/**
	 * Merges the given {@link Milestone}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Milestone}
	 * will be shown.
	 * 
	 * @param milestone the {@link Milestone} to merge
	 * @param bindingResult the binding results from building the given {@link Milestone}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
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

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Milestone} to update
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
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

	/**
	 * Deletes the {@link Milestone} with the given id. 
	 * @param id the id of the {@link Milestone} to delete 
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}/{phaseId}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @PathVariable("phaseId") Long phaseId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Phase phase = Phase.findPhase(phaseId);
		Milestone milestone = Milestone.findMilestone(id);
		phase.removeMilestone(milestone);
		phase.merge();
		milestone.remove();
		uiModel.asMap().clear();
		return "redirect:/showParentProject" + "?id="
				+ URLHelper.encodeUrlPathSegment(phaseId.toString(), httpServletRequest) + "&className="
				+ URLHelper.encodeUrlPathSegment(Phase.class.getName(), httpServletRequest);
	}

	private void populateEditForm(Model uiModel, Milestone milestone) {
		uiModel.addAttribute("milestone", milestone);
	}
}
