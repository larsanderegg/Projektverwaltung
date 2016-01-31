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

import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Project;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.PhaseService;
import ch.lan.teko.service.ProjectService;

@RequestMapping("/phases")
@Controller
@RooWebScaffold(path = "phases", formBackingObject = Phase.class)
public class PhaseController {

	@Autowired
    PhaseService phaseService;

	@Autowired
    ActivityService activityService;

	@Autowired
    DocumentReferenceService documentReferenceService;
	
	@Autowired
	ProjectService projectService;
	
	@RequestMapping(value = "/{id}", params = {"projectId"}, produces = "text/html")
    public String show(@PathVariable("id") Long id, @RequestParam(value = "projectId", required = true) Long projectId, Model uiModel) {
        uiModel.addAttribute("phase", phaseService.findPhase(id));
        uiModel.addAttribute("itemId", id);
        
        Project project = projectService.findProject(projectId);
		if(project != null){
			uiModel.addAttribute("project", project);
		}
        
        return "phases/show";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Phase phase, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, phase);
            return "phases/update";
        }
        uiModel.asMap().clear();
        phaseService.updatePhase(phase);
        return "redirect:/phases/" + encodeUrlPathSegment(phase.getId().toString(), httpServletRequest) +"?projectId="+encodeUrlPathSegment(phase.getProjectId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = {"form", "projectId"}, produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, @RequestParam(value = "projectId", required = true) Long projectId, Model uiModel) {
        Phase phase = phaseService.findPhase(id);
        phase.setProjectId(projectId);
		populateEditForm(uiModel, phase);
        
        Project project = projectService.findProject(projectId);
		if(project != null){
			uiModel.addAttribute("project", project);
		}
        
        return "phases/update";
    }

	void populateEditForm(Model uiModel, Phase phase) {
        uiModel.addAttribute("phase", phase);
        uiModel.addAttribute("activitys", activityService.findAllActivitys());
        uiModel.addAttribute("documentreferences", documentReferenceService.findAllDocumentReferences());
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
