package ch.lan.teko.controller;
import ch.lan.teko.model.Activity;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Resource;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.EmployeeService;
import ch.lan.teko.service.PhaseService;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/activitys")
@Controller
@RooWebScaffold(path = "activitys", formBackingObject = Activity.class)
public class ActivityController {

	@Autowired
    EmployeeService employeeService;

	@Autowired
    ActivityService activityService;

	@Autowired
    DocumentReferenceService documentReferenceService;
	
	@Autowired
	PhaseService phaseService;
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Activity activity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, activity);
            return "activitys/create";
        }
        uiModel.asMap().clear();
        activityService.saveActivity(activity);
        
        Phase phase = phaseService.findPhase(activity.getPhaseId());
        if(phase != null){
        	phase.getActivities().add(activity);
        	phaseService.savePhase(phase);
        } else {
        	return "error";
        }
        
        return "redirect:/activitys/" + encodeUrlPathSegment(activity.getId().toString(), httpServletRequest) +"?phaseId="+encodeUrlPathSegment(phase.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = {"form", "phaseId"}, produces = "text/html")
    public String createForm(@RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
        Activity activity = new Activity();
        activity.setPhaseId(phaseId);
        
		populateEditForm(uiModel, activity);
		
		Phase phase = phaseService.findPhase(phaseId);
		if(phase != null){
			uiModel.addAttribute("phase", phase);
		}
		
        List<String[]> dependencies = new ArrayList<String[]>();
        if (employeeService.countAllEmployees() == 0) {
            dependencies.add(new String[] { "responsible", "employees" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "activitys/create";
    }

	@RequestMapping(value = "/{id}", params = {"phaseId"}, produces = "text/html")
    public String show(@PathVariable("id") Long id, @RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
        uiModel.addAttribute("activity", activityService.findActivity(id));
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("phase", phaseService.findPhase(phaseId));
        return "activitys/show";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Activity activity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, activity);
            return "activitys/update";
        }
        uiModel.asMap().clear();
        activityService.updateActivity(activity);
        return "redirect:/activitys/" + encodeUrlPathSegment(activity.getId().toString(), httpServletRequest) +"?phaseId="+encodeUrlPathSegment(activity.getPhaseId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = {"form", "phaseId"}, produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, @RequestParam(value = "phaseId", required = true) Long phaseId, Model uiModel) {
        Activity activity = activityService.findActivity(id);
        activity.setPhaseId(phaseId);
		populateEditForm(uiModel, activity);
		
		Phase phase = phaseService.findPhase(phaseId);
		if(phase != null){
			uiModel.addAttribute("phase", phase);
		}
		
        return "activitys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Activity activity = activityService.findActivity(id);
        activityService.deleteActivity(activity);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/activitys";
    }

	void populateEditForm(Model uiModel, Activity activity) {
        uiModel.addAttribute("activity", activity);
        uiModel.addAttribute("documentreferences", documentReferenceService.findAllDocumentReferences());
        uiModel.addAttribute("employees", employeeService.findAllEmployees());
        uiModel.addAttribute("resources", Resource.findAllResources());
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
