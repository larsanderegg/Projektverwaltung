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

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Resource;
import ch.lan.teko.util.URLHelper;

/**
 * Controller for an {@link Activity}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/activitys")
@Controller
@GvNIXWebJQuery
public class ActivityController {

	/**
	 * Persist the given {@link Activity}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Activity}
	 * will be shown.
	 * 
	 * @param activity the {@link Activity} to persist
	 * @param bindingResult the binding results from building the given {@link Activity}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Activity activity, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, activity);
			return "activitys/create";
		}
		uiModel.asMap().clear();
		activity.persist();

		Phase phase = Phase.findPhase(activity.getPhaseId());
		if (phase != null) {
			phase.addActivity(activity);
			phase.merge();
		} else {
			return "error";
		}

		return "redirect:/activitys/" + URLHelper.encodeUrlPathSegment(activity.getId().toString(), httpServletRequest)
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
		Activity activity = new Activity();
		activity.setPhaseId(phaseId);

		populateEditForm(uiModel, activity);

		Phase phase = Phase.findPhase(phaseId);
		if (phase != null) {
			uiModel.addAttribute("phase", phase);
		}

		List<String[]> dependencies = new ArrayList<String[]>();
		if (Employee.countEmployees() == 0) {
			dependencies.add(new String[] { "responsible", "employees" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "activitys/create";
	}

	/**
	 * Gets the data to show a single {@link Activity}
	 * @param id the id of the {@link Activity}
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", params = { "phaseId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id, @RequestParam(value = "phaseId", required = true) Long phaseId,
			Model uiModel) {
		uiModel.addAttribute("activity", Activity.findActivity(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("phase", Phase.findPhase(phaseId));
		return "activitys/show";
	}

	/**
	 * Merges the given {@link Activity}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Activity}
	 * will be shown.
	 * 
	 * @param activity the {@link Activity} to merge
	 * @param bindingResult the binding results from building the given {@link Activity}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Activity activity, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, activity);
			return "activitys/update";
		}
		uiModel.asMap().clear();
		activity.merge();
		return "redirect:/activitys/" + URLHelper.encodeUrlPathSegment(activity.getId().toString(), httpServletRequest)
				+ "?phaseId=" + URLHelper.encodeUrlPathSegment(activity.getPhaseId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Activity} to update
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = { "form", "phaseId" }, produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
		Activity activity = Activity.findActivity(id);
		activity.setPhaseId(phaseId);
		populateEditForm(uiModel, activity);

		Phase phase = Phase.findPhase(phaseId);
		if (phase != null) {
			uiModel.addAttribute("phase", phase);
		}

		return "activitys/update";
	}

	/**
	 * Deletes the {@link Activity} with the given id. 
	 * @param id the id of the {@link Activity} to delete 
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}/{phaseId}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @PathVariable("phaseId") Long phaseId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Phase phase = Phase.findPhase(phaseId);
		Activity activity = Activity.findActivity(id);
		phase.removeActivity(activity);
		phase.merge();
		activity.remove();
		uiModel.asMap().clear();
		return "redirect:/showParentProject" + "?id="
				+ URLHelper.encodeUrlPathSegment(phaseId.toString(), httpServletRequest) + "&className="
				+ URLHelper.encodeUrlPathSegment(Phase.class.getName(), httpServletRequest);
	}

	private void populateEditForm(Model uiModel, Activity activity) {
		uiModel.addAttribute("activity", activity);
		uiModel.addAttribute("documentreferences", DocumentReference.findAllDocumentReferences());
		uiModel.addAttribute("employees", Employee.findAllEmployees());
		uiModel.addAttribute("resources", Resource.findAllResources());
	}
}
