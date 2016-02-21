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

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Project;
import ch.lan.teko.util.URLHelper;

/**
 * Controller for a {@link DocumentReference}. Handles the web requests and returns the view to show the response.
 * @author landeregg
 * 
 */
@RequestMapping("/documentreferences")
@Controller
@GvNIXWebJQuery
public class DocumentReferenceController {
	
	/**
	 * Persist the given {@link DocumentReference}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link DocumentReference}
	 * will be shown.
	 * 
	 * @param documentReference the {@link DocumentReference} to persist
	 * @param bindingResult the binding results from building the given {@link DocumentReference}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid DocumentReference documentReference, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, documentReference);
			return "documentreferences/create";
		}
		uiModel.asMap().clear();
		documentReference.persist();

		return createRedirectLink(documentReference, httpServletRequest);
	}

	/**
	 * Gets the data for the create form view.
	 * @param activityId the parent activity id
	 * @param projectId the parent project id
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(@RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {

		DocumentReference documentReference = new DocumentReference();
		populateEditForm(uiModel, documentReference);

		if (activityId != null) {
			uiModel.addAttribute("activity", Activity.findActivity(activityId));
			documentReference.setActivityId(activityId);
		} else if (projectId != null) {
			uiModel.addAttribute("project", Project.findProject(projectId));
			documentReference.setProjectId(projectId);
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", Phase.findPhase(phaseId));
			documentReference.setPhaseId(phaseId);
		}

		return "documentreferences/create";
	}

	/**
	 * Gets the data to show a single {@link DocumentReference}
	 * @param id the id of the {@link DocumentReference}
	 * @param activityId the parent activity id
	 * @param projectId the parent project id
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the view
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {
		uiModel.addAttribute("documentreference", DocumentReference.findDocumentReference(id));
		uiModel.addAttribute("itemId", id);

		if (activityId != null) {
			uiModel.addAttribute("activity", Activity.findActivity(activityId));
		} else if (projectId != null) {
			uiModel.addAttribute("project", Project.findProject(projectId));
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", Phase.findPhase(phaseId));
		}

		return "documentreferences/show";
	}

	/**
	 * Merges the given {@link DocumentReference}. In case of an error an error view
	 * will be shown. If everything was fine, the persisted {@link DocumentReference}
	 * will be shown.
	 * 
	 * @param documentReference the {@link DocumentReference} to merge
	 * @param bindingResult the binding results from building the given {@link DocumentReference}
	 * @param uiModel the model for the new view
	 * @param httpServletRequest the whole request
	 * @return the name of the new view
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid DocumentReference documentReference, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, documentReference);
			return "documentreferences/update";
		}
		uiModel.asMap().clear();
		documentReference.merge();

		return createRedirectLink(documentReference, httpServletRequest);
	}

	/**
	 * Gets the data for the update form view.
	 * @param id the id of the {@link Phase} to update
	 * @param activityId the parent activity id
	 * @param projectId the parent project id
	 * @param phaseId the parent phase id
	 * @param uiModel the model of the form view
	 * @return the name of the form view
	 */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id,
			@RequestParam(value = "activityId", required = false) Long activityId,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "phaseId", required = false) Long phaseId, Model uiModel) {
		DocumentReference documentReference = DocumentReference.findDocumentReference(id);
		populateEditForm(uiModel, documentReference);

		if (activityId != null) {
			uiModel.addAttribute("activity", Activity.findActivity(activityId));
			documentReference.setActivityId(activityId);
		} else if (projectId != null) {
			uiModel.addAttribute("project", Project.findProject(projectId));
			documentReference.setProjectId(projectId);
		} else if (phaseId != null) {
			uiModel.addAttribute("phase", Phase.findPhase(phaseId));
			documentReference.setPhaseId(phaseId);
		}

		return "documentreferences/update";
	}

	/**
	 * Deletes the {@link DocumentReference} with the given id. 
	 * @param id the id of the {@link DocumentReference} to delete 
	 * @param uiModel the model of the new view
	 * @return the name of the new view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, Model uiModel) {
		DocumentReference documentReference = DocumentReference.findDocumentReference(id);
		documentReference.remove();
		uiModel.asMap().clear();
		return "redirect:/documentreferences";
	}

	private String createRedirectLink(DocumentReference documentReference, HttpServletRequest httpServletRequest) {

		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append("redirect:/documentreferences/"
				+ URLHelper.encodeUrlPathSegment(documentReference.getId().toString(), httpServletRequest));
		if (documentReference.getActivityId() != null) {
			resultBuilder.append("?activityId="
					+ URLHelper.encodeUrlPathSegment(documentReference.getActivityId().toString(), httpServletRequest));
		} else if (documentReference.getProjectId() != null) {
			resultBuilder.append("?projectId="
					+ URLHelper.encodeUrlPathSegment(documentReference.getProjectId().toString(), httpServletRequest));
		} else if (documentReference.getPhaseId() != null) {
			resultBuilder.append(
					"?phaseId=" + URLHelper.encodeUrlPathSegment(documentReference.getPhaseId().toString(), httpServletRequest));
		}

		return resultBuilder.toString();
	}

	private void populateEditForm(Model uiModel, DocumentReference documentReference) {
		uiModel.addAttribute("documentReference", documentReference);
	}
}
