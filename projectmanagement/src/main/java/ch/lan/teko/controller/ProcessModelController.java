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


/**
 * Controller for an {@link ProcessModel}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 */
@RequestMapping("/processmodels")
@Controller
@GvNIXWebJQuery
public class ProcessModelController {
	
	/**
	 * Persist the given {@link ProcessModel}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link ProcessModel}
	 * will be shown.
	 * 
	 * @param processModel the {@link ProcessModel} to persist
	 * @param bindingResult the binding results from building the given {@link ProcessModel}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
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

	/**
	 * Gets the data for the create form view.
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new ProcessModel());
		return "processmodels/create";
	}

	/**
	 * Gets the data to show a single {@link ProcessModel}
	 * @param id the id of the {@link ProcessModel}
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("processmodel", ProcessModel.findProcessModel(id));
		uiModel.addAttribute("itemId", id);
		return "processmodels/show";
	}

	/**
	 * Gets the data to show a list of {@link ProcessModel}
	 * @param page in case of paging, the page index
	 * @param size in case of paging, the size of show process models
	 * @param sortFieldName in case of sorting, the field name which should be sorted
	 * @param sortOrder in case of sorting, the sort order
	 * @param uiModel the model of the list view
	 * @return the name of the list view
	 */
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

	/**
	 * Merges the given {@link ProcessModel}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link ProcessModel}
	 * will be shown.
	 * 
	 * @param processModel the {@link ProcessModel} to merge
	 * @param bindingResult the binding results from building the given {@link ProcessModel}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
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

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link ProcessModel} to update
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, ProcessModel.findProcessModel(id));
		return "processmodels/update";
	}

	/**
	 * Deletes the {@link ProcessModel} with the given id. 
	 * @param id the id of the {@link ProcessModel} to delete 
	 * @param uiModel the model of the new view
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, Model uiModel) {
		ProcessModel processModel = ProcessModel.findProcessModel(id);
		processModel.remove();
		uiModel.asMap().clear();
		return "redirect:/processmodels";
	}

	private void populateEditForm(Model uiModel, ProcessModel processModel) {
		uiModel.addAttribute("processModel", processModel);
	}
}
