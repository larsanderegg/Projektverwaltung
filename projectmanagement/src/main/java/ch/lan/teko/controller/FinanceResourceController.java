package ch.lan.teko.controller;
import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.service.FinanceResourceService;
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

@RequestMapping("/financeresources")
@Controller
@RooWebScaffold(path = "financeresources", formBackingObject = FinanceResource.class)
public class FinanceResourceController {

	@Autowired
    FinanceResourceService financeResourceService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, financeResource);
            return "financeresources/create";
        }
        uiModel.asMap().clear();
        financeResourceService.saveFinanceResource(financeResource);
        return "redirect:/financeresources/" + encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new FinanceResource());
        return "financeresources/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("financeresource", financeResourceService.findFinanceResource(id));
        uiModel.addAttribute("itemId", id);
        return "financeresources/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("financeresources", FinanceResource.findFinanceResourceEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) financeResourceService.countAllFinanceResources() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("financeresources", FinanceResource.findAllFinanceResources(sortFieldName, sortOrder));
        }
        return "financeresources/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid FinanceResource financeResource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, financeResource);
            return "financeresources/update";
        }
        uiModel.asMap().clear();
        financeResourceService.updateFinanceResource(financeResource);
        return "redirect:/financeresources/" + encodeUrlPathSegment(financeResource.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, financeResourceService.findFinanceResource(id));
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
