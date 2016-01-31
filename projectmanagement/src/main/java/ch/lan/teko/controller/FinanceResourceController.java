package ch.lan.teko.controller;
import java.io.UnsupportedEncodingException;

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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.FinanceResourceService;

@RequestMapping("/financeresources")
@Controller
@RooWebScaffold(path = "financeresources", formBackingObject = FinanceResource.class)
public class FinanceResourceController {

	@Autowired
    FinanceResourceService financeResourceService;
	
	@Autowired
    ActivityService activityService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, financeResource);
            return "financeresources/create";
        }
        uiModel.asMap().clear();
        financeResourceService.saveFinanceResource(financeResource);
        
        Activity activity = activityService.findActivity(financeResource.getActivityId());
        if(activity != null){
        	activity.getResources().add(financeResource);
        	activityService.saveActivity(activity);
        } else {
        	return "error";
        }
        
        return "redirect:/financeresources/" + encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest) +"?activityId="+encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = {"form", "activityId"}, produces = "text/html")
    public String createForm(@RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
        FinanceResource financeResource = new FinanceResource();
        financeResource.setActivityId(activityId);
		populateEditForm(uiModel, financeResource);
		
		Activity activity = activityService.findActivity(activityId);
        if(activity != null){
        	uiModel.addAttribute("activity", activity);
        }
		
        return "financeresources/create";
    }

	@RequestMapping(value = "/{id}", params = {"activityId"}, produces = "text/html")
    public String show(@PathVariable("id") Long id, @RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
        uiModel.addAttribute("financeresource", financeResourceService.findFinanceResource(id));
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("activity", activityService.findActivity(activityId));
        return "financeresources/show";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, financeResource);
            return "financeresources/update";
        }
        uiModel.asMap().clear();
        financeResourceService.updateFinanceResource(financeResource);
        return "redirect:/financeresources/" + encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest) +"?activityId="+encodeUrlPathSegment(financeResource.getActivityId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = {"form", "activityId"}, produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, @RequestParam(value = "activityId", required = true) Long activityId, Model uiModel) {
		
        FinanceResource financeResource = financeResourceService.findFinanceResource(id);
        financeResource.setActivityId(activityId);
		populateEditForm(uiModel, financeResource);
		
		Activity activity = activityService.findActivity(activityId);
        if(activity != null){
        	uiModel.addAttribute("activity", activity);
        }
		
        return "financeresources/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FinanceResource financeResource = financeResourceService.findFinanceResource(id);
        financeResourceService.deleteFinanceResource(financeResource);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/financeresources";
    }

	void populateEditForm(Model uiModel, FinanceResource financeResource) {
        uiModel.addAttribute("financeResource", financeResource);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
