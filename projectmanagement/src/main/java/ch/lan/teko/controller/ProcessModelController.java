package ch.lan.teko.controller;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.util.URLHelper;

@RequestMapping("/processmodels")
@Controller
@GvNIXWebJQuery
public class ProcessModelController {
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid ProcessModel processModel, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, processModel);
			return "processmodels/create";
		}
		uiModel.asMap().clear();
		processModel.persist();
		return "redirect:/processmodels/"
				+ URLHelper.encodeUrlPathSegment(processModel.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new ProcessModel());
		return "processmodels/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("processmodel", ProcessModel.findProcessModel(id));
		uiModel.addAttribute("itemId", id);
		return "processmodels/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("processmodels",
					ProcessModel.findProcessModelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) ProcessModel.countProcessModels() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("processmodels", ProcessModel.findAllProcessModels(sortFieldName, sortOrder));
		}
		return "processmodels/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid ProcessModel processModel, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, processModel);
			return "processmodels/update";
		}
		uiModel.asMap().clear();
		processModel.merge();
		return "redirect:/processmodels/"
				+ URLHelper.encodeUrlPathSegment(processModel.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, ProcessModel.findProcessModel(id));
		return "processmodels/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		ProcessModel processModel = ProcessModel.findProcessModel(id);
		processModel.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/processmodels";
	}

	void populateEditForm(Model uiModel, ProcessModel processModel) {
		uiModel.addAttribute("processModel", processModel);
	}
}
