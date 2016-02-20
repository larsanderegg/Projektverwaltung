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

@RequestMapping("/financeresources")
@Controller
@GvNIXWebJQuery
public class FinanceResourceController {
	
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

	@RequestMapping(value = "/{id}", params = { "activityId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		uiModel.addAttribute("financeresource", FinanceResource.findFinanceResource(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("activity", Activity.findActivity(activityId));
		return "financeresources/show";
	}

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

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		FinanceResource financeResource = FinanceResource.findFinanceResource(id);
		financeResource.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/financeresources";
	}

	void populateEditForm(Model uiModel, FinanceResource financeResource) {
		uiModel.addAttribute("financeResource", financeResource);
	}
}
