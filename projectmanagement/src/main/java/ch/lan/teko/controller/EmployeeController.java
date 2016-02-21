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

import ch.lan.teko.model.Employee;
import ch.lan.teko.util.URLHelper;

/**
 * Controller for a {@link Employee}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/employees")
@Controller
@GvNIXWebJQuery
public class EmployeeController {
	
	/**
	 * Persist the given {@link Employee}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Employee}
	 * will be shown.
	 * 
	 * @param employee the {@link Employee} to persist
	 * @param bindingResult the binding results from building the given {@link Employee}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Employee employee, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, employee);
			return "employees/create";
		}
		uiModel.asMap().clear();
		employee.persist();
		return "redirect:/employees/" + URLHelper.encodeUrlPathSegment(employee.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the create form view.
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Employee());
		return "employees/create";
	}

	/**
	 * Gets the data to show a single {@link Employee}
	 * @param id the id of the {@link Employee}
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("employee", Employee.findEmployee(id));
		uiModel.addAttribute("itemId", id);
		return "employees/show";
	}

	/**
	 * Gets the data to show a list of {@link Employee}
	 * @param page in case of paging, the page index
	 * @param size in case of paging, the size of show employees
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
			uiModel.addAttribute("employees",
					Employee.findEmployeeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) Employee.countEmployees() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("employees", Employee.findAllEmployees(sortFieldName, sortOrder));
		}
		return "employees/list";
	}

	/**
	 * Merges the given {@link Employee}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link Employee}
	 * will be shown.
	 * 
	 * @param employee the {@link Employee} to merge
	 * @param bindingResult the binding results from building the given {@link Employee}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Employee employee, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, employee);
			return "employees/update";
		}
		uiModel.asMap().clear();
		employee.merge();
		return "redirect:/employees/" + URLHelper.encodeUrlPathSegment(employee.getId().toString(), httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Employee} to update
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Employee.findEmployee(id));
		return "employees/update";
	}
	
	/**
	 * Deletes the {@link Employee} with the given id. 
	 * @param id the id of the {@link Employee} to delete 
	 * @param uiModel the model of the new view
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, Model uiModel) {
		Employee employee = Employee.findEmployee(id);
		employee.remove();
		uiModel.asMap().clear();
		return "redirect:/employees";
	}

	private void populateEditForm(Model uiModel, Employee employee) {
		uiModel.addAttribute("employee", employee);
	}
}
