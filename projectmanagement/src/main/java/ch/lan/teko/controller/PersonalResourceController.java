package ch.lan.teko.controller;
import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.service.EmployeeService;
import ch.lan.teko.service.PersonalResourceService;
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

@RequestMapping("/personalresources")
@Controller
@RooWebScaffold(path = "personalresources", formBackingObject = PersonalResource.class)
public class PersonalResourceController {

	@Autowired
    EmployeeService employeeService;

	@Autowired
    PersonalResourceService personalResourceService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalResource);
            return "personalresources/create";
        }
        uiModel.asMap().clear();
        personalResourceService.savePersonalResource(personalResource);
        return "redirect:/personalresources/" + encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new PersonalResource());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (employeeService.countAllEmployees() == 0) {
            dependencies.add(new String[] { "employee", "employees" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "personalresources/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("personalresource", personalResourceService.findPersonalResource(id));
        uiModel.addAttribute("itemId", id);
        return "personalresources/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("personalresources", PersonalResource.findPersonalResourceEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) personalResourceService.countAllPersonalResources() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("personalresources", PersonalResource.findAllPersonalResources(sortFieldName, sortOrder));
        }
        return "personalresources/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalResource);
            return "personalresources/update";
        }
        uiModel.asMap().clear();
        personalResourceService.updatePersonalResource(personalResource);
        return "redirect:/personalresources/" + encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, personalResourceService.findPersonalResource(id));
        return "personalresources/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
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
