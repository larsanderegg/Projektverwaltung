package ch.lan.teko.controller;
import ch.lan.teko.model.Milestone;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/milestones")
@Controller
@RooWebScaffold(path = "milestones", formBackingObject = Milestone.class)
public class MilestoneController {
}
