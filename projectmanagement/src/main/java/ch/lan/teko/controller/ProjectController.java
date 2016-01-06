package ch.lan.teko.controller;
import ch.lan.teko.model.Project;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/projects")
@Controller
@RooWebScaffold(path = "projects", formBackingObject = Project.class)
public class ProjectController {
}
