package ch.lan.teko.controller;
import ch.lan.teko.model.Phase;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/phases")
@Controller
@RooWebScaffold(path = "phases", formBackingObject = Phase.class)
public class PhaseController {
}
