package ch.lan.teko.controller;
import ch.lan.teko.model.ProcessModel;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/processmodels")
@Controller
@RooWebScaffold(path = "processmodels", formBackingObject = ProcessModel.class)
public class ProcessModelController {
}
