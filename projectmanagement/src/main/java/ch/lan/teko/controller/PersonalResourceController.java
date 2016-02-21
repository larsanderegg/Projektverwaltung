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
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.util.URLHelper;


/**
 * Controller for an {@link PersonalResource}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/personalresources")
@Controller
@GvNIXWebJQuery
public class PersonalResourceController {
	
	/**
	 * Persist the given {@link PersonalResource}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link PersonalResource}
	 * will be shown.
	 * 
	 * @param personalResource the {@link PersonalResource} to persist
	 * @param bindingResult the binding results from building the given {@link PersonalResource}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, personalResource);
			return "personalresources/create";
		}
		uiModel.asMap().clear();
		personalResource.persist();

		Activity activity = Activity.findActivity(personalResource.getActivityId());
		if (activity != null) {
			activity.addResource(personalResource);
			activity.merge();
		} else {
			return "error";
		}

		return "redirect:/personalresources/"
				+ URLHelper.encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest)
				+ "?activityId=" + URLHelper.encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the create form view.
	 * @param activityId the parent activity id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = { "form", "activityId" }, produces = "text/html")
	public String createForm(@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		PersonalResource personalResource = new PersonalResource();
		personalResource.setActivityId(activityId);
		
		populateEditForm(uiModel, personalResource);

		Activity activity = Activity.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		List<String[]> dependencies = new ArrayList<String[]>();
		if (Employee.countEmployees() == 0) {
			dependencies.add(new String[] { "employee", "employees" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "personalresources/create";
	}

	/**
	 * Gets the data to show a single {@link PersonalResource}
	 * @param id the id of the {@link PersonalResource}
	 * @param activityId the parent activity id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", params = { "activityId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		uiModel.addAttribute("personalresource", PersonalResource.findPersonalResource(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("activity", Activity.findActivity(activityId));
		return "personalresources/show";
	}

	/**
	 * Merges the given {@link PersonalResource}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link PersonalResource}
	 * will be shown.
	 * 
	 * @param personalResource the {@link PersonalResource} to merge
	 * @param bindingResult the binding results from building the given {@link PersonalResource}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, personalResource);
			return "personalresources/update";
		}
		uiModel.asMap().clear();
		personalResource.merge();
		return "redirect:/personalresources/"
				+ URLHelper.encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest) + "?activityId="
				+ URLHelper.encodeUrlPathSegment(personalResource.getActivityId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link PersonalResource} to update
	 * @param activityId the parent activity id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = { "form", "activityId" }, produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		PersonalResource personalResource = PersonalResource.findPersonalResource(id);
		personalResource.setActivityId(activityId);
		populateEditForm(uiModel, personalResource);

		Activity activity = Activity.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		return "personalresources/update";
	}

	/**
	 * Deletes the {@link PersonalResource} with the given id. 
	 * @param id the id of the {@link PersonalResource} to delete 
	 * @param activityId the parent activity id
	 * @param uiModel the model of the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}/{activityId}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @PathVariable("activityId") Long activityId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Activity activity = Activity.findActivity(activityId);
		PersonalResource personalResource = PersonalResource.findPersonalResource(id);
		activity.removeResource(personalResource);
		activity.merge();
		personalResource.remove();
		uiModel.asMap().clear();
		return "redirect:/showParentProject" + "?id="
				+ URLHelper.encodeUrlPathSegment(activityId.toString(), httpServletRequest) + "&className="
				+ URLHelper.encodeUrlPathSegment(Activity.class.getName(), httpServletRequest);
	}

	private void populateEditForm(Model uiModel, PersonalResource personalResource) {
		uiModel.addAttribute("personalResource", personalResource);
		uiModel.addAttribute("employees", Employee.findAllEmployees());
	}
}
