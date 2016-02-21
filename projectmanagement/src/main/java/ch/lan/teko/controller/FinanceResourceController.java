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
import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.util.URLHelper;


/**
 * Controller for an {@link FinanceResource}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/financeresources")
@Controller
@GvNIXWebJQuery
public class FinanceResourceController {
	
	/**
	 * Persist the given {@link FinanceResource}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link FinanceResource}
	 * will be shown.
	 * 
	 * @param financeResource the {@link FinanceResource} to persist
	 * @param bindingResult the binding results from building the given {@link FinanceResource}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, financeResource);
			return "financeresources/create";
		}
		uiModel.asMap().clear();
		financeResource.persist();

		Activity activity = Activity.findActivity(financeResource.getActivityId());
		if (activity != null) {
			activity.addResource(financeResource);
			activity.merge();
		} else {
			return "error";
		}

		return "redirect:/financeresources/"
				+ URLHelper.encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest) + "?activityId="
				+ URLHelper.encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the create form view.
	 * @param activityId the parent activity id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = { "form", "activityId" }, produces = "text/html")
	public String createForm(@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		FinanceResource financeResource = new FinanceResource();
		financeResource.setActivityId(activityId);
		populateEditForm(uiModel, financeResource);

		Activity activity = Activity.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		return "financeresources/create";
	}

	/**
	 * Gets the data to show a single {@link FinanceResource}
	 * @param id the id of the {@link FinanceResource}
	 * @param activityId the parent activity id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", params = { "activityId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		uiModel.addAttribute("financeresource", FinanceResource.findFinanceResource(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("activity", Activity.findActivity(activityId));
		return "financeresources/show";
	}

	/**
	 * Merges the given {@link FinanceResource}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link FinanceResource}
	 * will be shown.
	 * 
	 * @param financeResource the {@link FinanceResource} to merge
	 * @param bindingResult the binding results from building the given {@link FinanceResource}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, financeResource);
			return "financeresources/update";
		}
		uiModel.asMap().clear();
		financeResource.merge();
		return "redirect:/financeresources/"
				+ URLHelper.encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest) + "?activityId="
				+ URLHelper.encodeUrlPathSegment(financeResource.getActivityId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link FinanceResource} to update
	 * @param activityId the parent activity id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = { "form", "activityId" }, produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {

		FinanceResource financeResource = FinanceResource.findFinanceResource(id);
		financeResource.setActivityId(activityId);
		populateEditForm(uiModel, financeResource);

		Activity activity = Activity.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		return "financeresources/update";
	}

	/**
	 * Deletes the {@link FinanceResource} with the given id.
	 * 
	 * @param id the id of the {@link FinanceResource} to delete
	 * @param activityId the parent activity id
	 * @param uiModel the model of the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}/{activityId}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@PathVariable("activityId") Long activityId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Activity activity = Activity.findActivity(activityId);
		FinanceResource financeResource = FinanceResource.findFinanceResource(id);
		activity.removeResource(financeResource);
		activity.merge();
		financeResource.remove();
		uiModel.asMap().clear();
		return "redirect:/showParentProject" + "?id="
				+ URLHelper.encodeUrlPathSegment(activityId.toString(), httpServletRequest)
				+ "&className="
				+ URLHelper.encodeUrlPathSegment(Activity.class.getName(), httpServletRequest);
	}

	private void populateEditForm(Model uiModel, FinanceResource financeResource) {
		uiModel.addAttribute("financeResource", financeResource);
	}
}
