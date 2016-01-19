package ch.lan.teko.controller;
import ch.lan.teko.model.Activity;
import ch.lan.teko.model.Resource;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.EmployeeService;
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

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Activity activity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, activity);
            return "activitys/create";
        }
        uiModel.asMap().clear();
        activityService.saveActivity(activity);
        return "redirect:/activitys/" + encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Activity());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (employeeService.countAllEmployees() == 0) {
            dependencies.add(new String[] { "responsible", "employees" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "activitys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("activity", activityService.findActivity(id));
        uiModel.addAttribute("itemId", id);
        return "activitys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("activitys", Activity.findActivityEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) activityService.countAllActivitys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("activitys", Activity.findAllActivitys(sortFieldName, sortOrder));
        }
        return "activitys/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Activity activity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, activity);
            return "activitys/update";
        }
        uiModel.asMap().clear();
        activityService.updateActivity(activity);
        return "redirect:/activitys/" + encodeUrlPathSegment(activity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, activityService.findActivity(id));
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
