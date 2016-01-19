package ch.lan.teko.controller;
import ch.lan.teko.model.Phase;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.PhaseService;
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

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Phase phase, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, phase);
            return "phases/create";
        }
        uiModel.asMap().clear();
        phaseService.savePhase(phase);
        return "redirect:/phases/" + encodeUrlPathSegment(phase.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Phase());
        return "phases/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("phase", phaseService.findPhase(id));
        uiModel.addAttribute("itemId", id);
        return "phases/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("phases", Phase.findPhaseEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) phaseService.countAllPhases() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("phases", Phase.findAllPhases(sortFieldName, sortOrder));
        }
        return "phases/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Phase phase, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, phase);
            return "phases/update";
        }
        uiModel.asMap().clear();
        phaseService.updatePhase(phase);
        return "redirect:/phases/" + encodeUrlPathSegment(phase.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, phaseService.findPhase(id));
        return "phases/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Phase phase = phaseService.findPhase(id);
        phaseService.deletePhase(phase);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/phases";
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
