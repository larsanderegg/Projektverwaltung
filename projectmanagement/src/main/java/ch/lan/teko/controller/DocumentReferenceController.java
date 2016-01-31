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

import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.PhaseService;
import ch.lan.teko.service.ProjectService;

@RequestMapping("/documentreferences")
@Controller
@RooWebScaffold(path = "documentreferences", formBackingObject = DocumentReference.class)
public class DocumentReferenceController {

	@Autowired
    DocumentReferenceService documentReferenceService;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	PhaseService phaseService;
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DocumentReference documentReference, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, documentReference);
            return "documentreferences/create";
        }
        uiModel.asMap().clear();
        documentReferenceService.saveDocumentReference(documentReference);
        
        return createRedirectLink(documentReference, httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(@RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {
		
		DocumentReference documentReference = new DocumentReference();
		populateEditForm(uiModel, documentReference);
		
		if (activityId != null) {
			uiModel.addAttribute("activity", activityService.findActivity(activityId));
			documentReference.setActivityId(activityId);
		} else if (projectId != null) {
			uiModel.addAttribute("project", projectService.findProject(projectId));
			documentReference.setProjectId(projectId);
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", phaseService.findPhase(phaseId));
			documentReference.setPhaseId(phaseId);
		}
		
		return "documentreferences/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, @RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {
        uiModel.addAttribute("documentreference", documentReferenceService.findDocumentReference(id));
        uiModel.addAttribute("itemId", id);
        
        if (activityId != null) {
			uiModel.addAttribute("activity", activityService.findActivity(activityId));
		} else if (projectId != null) {
			uiModel.addAttribute("project", projectService.findProject(projectId));
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", phaseService.findPhase(phaseId));
		}
        
        return "documentreferences/show";
    }

//	@RequestMapping(produces = "text/html")
//    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
//        if (page != null || size != null) {
//            int sizeNo = size == null ? 10 : size.intValue();
//            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//            uiModel.addAttribute("documentreferences", DocumentReference.findDocumentReferenceEntries(firstResult, sizeNo, sortFieldName, sortOrder));
//            float nrOfPages = (float) documentReferenceService.countAllDocumentReferences() / sizeNo;
//            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
//        } else {
//            uiModel.addAttribute("documentreferences", DocumentReference.findAllDocumentReferences(sortFieldName, sortOrder));
//        }
//        return "documentreferences/list";
//    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DocumentReference documentReference, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, documentReference);
            return "documentreferences/update";
        }
        uiModel.asMap().clear();
        documentReferenceService.updateDocumentReference(documentReference);
        
        return createRedirectLink(documentReference, httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, @RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {
        DocumentReference documentReference = documentReferenceService.findDocumentReference(id);
		populateEditForm(uiModel, documentReference);
        
        if (activityId != null) {
			uiModel.addAttribute("activity", activityService.findActivity(activityId));
			documentReference.setActivityId(activityId);
		} else if (projectId != null) {
			uiModel.addAttribute("project", projectService.findProject(projectId));
			documentReference.setProjectId(projectId);
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", phaseService.findPhase(phaseId));
			documentReference.setPhaseId(phaseId);
		}
        
        return "documentreferences/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DocumentReference documentReference = documentReferenceService.findDocumentReference(id);
        documentReferenceService.deleteDocumentReference(documentReference);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/documentreferences";
    }
	
	private String createRedirectLink(DocumentReference documentReference, HttpServletRequest httpServletRequest){
		
		StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("redirect:/documentreferences/" + encodeUrlPathSegment(documentReference.getId().toString(), httpServletRequest));
        if (documentReference.getActivityId() != null) {
        	resultBuilder.append("?activityId="+encodeUrlPathSegment(documentReference.getActivityId().toString(), httpServletRequest));
		} else if (documentReference.getProjectId() != null) {
			resultBuilder.append("?projectId="+encodeUrlPathSegment(documentReference.getProjectId().toString(), httpServletRequest));
		} else if (documentReference.getPhaseId() != null) {
			resultBuilder.append("?phaseId="+encodeUrlPathSegment(documentReference.getPhaseId().toString(), httpServletRequest));
		}
        
		return resultBuilder.toString();
	}

	void populateEditForm(Model uiModel, DocumentReference documentReference) {
        uiModel.addAttribute("documentReference", documentReference);
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
