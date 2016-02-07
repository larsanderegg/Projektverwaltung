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

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.EmployeeService;
import ch.lan.teko.service.PersonalResourceService;
import ch.lan.teko.util.URLHelper;

@RequestMapping("/personalresources")
@Controller
@RooWebScaffold(path = "personalresources", formBackingObject = PersonalResource.class)
public class PersonalResourceController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	PersonalResourceService personalResourceService;

	@Autowired
	ActivityService activityService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, personalResource);
			return "personalresources/create";
		}
		uiModel.asMap().clear();
		personalResourceService.savePersonalResource(personalResource);

		Activity activity = activityService.findActivity(personalResource.getActivityId());
		if (activity != null) {
			activity.getResources().add(personalResource);
			activityService.saveActivity(activity);
		} else {
			return "error";
		}

		return "redirect:/personalresources/"
				+ URLHelper.encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest)
				+ "?activityId=" + URLHelper.encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = { "form", "activityId" }, produces = "text/html")
	public String createForm(@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		PersonalResource personalResource = new PersonalResource();
		personalResource.setActivityId(activityId);
		
		populateEditForm(uiModel, personalResource);

		Activity activity = activityService.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		List<String[]> dependencies = new ArrayList<String[]>();
		if (employeeService.countAllEmployees() == 0) {
			dependencies.add(new String[] { "employee", "employees" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "personalresources/create";
	}

	@RequestMapping(value = "/{id}", params = { "activityId" }, produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		uiModel.addAttribute("personalresource", personalResourceService.findPersonalResource(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("activity", activityService.findActivity(activityId));
		return "personalresources/show";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, personalResource);
			return "personalresources/update";
		}
		uiModel.asMap().clear();
		personalResourceService.updatePersonalResource(personalResource);
		return "redirect:/personalresources/"
				+ URLHelper.encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest) + "?activityId="
				+ URLHelper.encodeUrlPathSegment(personalResource.getActivityId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		PersonalResource personalResource = personalResourceService.findPersonalResource(id);
		personalResource.setActivityId(activityId);
		populateEditForm(uiModel, personalResource);

		Activity activity = activityService.findActivity(activityId);
		if (activity != null) {
			uiModel.addAttribute("activity", activity);
		}

		return "personalresources/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		PersonalResource personalResource = personalResourceService.findPersonalResource(id);
		personalResourceService.deletePersonalResource(personalResource);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/personalresources";
	}

	void populateEditForm(Model uiModel, PersonalResource personalResource) {
		uiModel.addAttribute("personalResource", personalResource);
		uiModel.addAttribute("employees", employeeService.findAllEmployees());
	}
}
