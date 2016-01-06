// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.lan.teko.controller;

import ch.lan.teko.controller.PersonalResourceController;
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.PersonalResource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect PersonalResourceController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PersonalResourceController.create(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalResource);
            return "personalresources/create";
        }
        uiModel.asMap().clear();
        personalResource.persist();
        return "redirect:/personalresources/" + encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PersonalResourceController.createForm(Model uiModel) {
        populateEditForm(uiModel, new PersonalResource());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Employee.countEmployees() == 0) {
            dependencies.add(new String[] { "employee", "employees" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "personalresources/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PersonalResourceController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("personalresource", PersonalResource.findPersonalResource(id));
        uiModel.addAttribute("itemId", id);
        return "personalresources/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PersonalResourceController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("personalresources", PersonalResource.findPersonalResourceEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) PersonalResource.countPersonalResources() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("personalresources", PersonalResource.findAllPersonalResources(sortFieldName, sortOrder));
        }
        return "personalresources/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PersonalResourceController.update(@Valid PersonalResource personalResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalResource);
            return "personalresources/update";
        }
        uiModel.asMap().clear();
        personalResource.merge();
        return "redirect:/personalresources/" + encodeUrlPathSegment(personalResource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PersonalResourceController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, PersonalResource.findPersonalResource(id));
        return "personalresources/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PersonalResourceController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        PersonalResource personalResource = PersonalResource.findPersonalResource(id);
        personalResource.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/personalresources";
    }
    
    void PersonalResourceController.populateEditForm(Model uiModel, PersonalResource personalResource) {
        uiModel.addAttribute("personalResource", personalResource);
        uiModel.addAttribute("employees", Employee.findAllEmployees());
    }
    
    String PersonalResourceController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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