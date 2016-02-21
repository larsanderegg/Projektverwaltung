package ch.lan.teko.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.Project;
import ch.lan.teko.util.URLHelper;

/**
 * Controller to show the parent projects overview.
 * @author landeregg
 */
@RequestMapping("/showParentProject")
@Controller
public class ShowParentProjectController {
	
	/**
	 * Gets the parent project for a phase or an activity.
	 * @param className the class name of a phase or an activity
	 * @param id the id of a phase or an activity
	 * @param httpServletRequest the whole request
	 * @return the name of the projects overview view
	 */
	@RequestMapping(params = { "className", "id" }, produces = "text/html")
	public String show(@RequestParam(value = "className", required = true) String className,
			@RequestParam(value = "id", required = true) Long id,
			HttpServletRequest httpServletRequest) {

		Long projectId = null;
		
		
		if(className.equals(Activity.class.getName())){
			Project project = Project.findProjectByActivityId(id);
			projectId = project.getId();
		} else if(className.equals(Phase.class.getName())){
			Project project = Project.findProjectByPhaseId(id);
			projectId = project.getId();
		}
		
		if (projectId != null) {
			return "redirect:/projects/" + URLHelper.encodeUrlPathSegment(projectId.toString(), httpServletRequest);
		} 
		return "redirect:/projects";
	}

}
