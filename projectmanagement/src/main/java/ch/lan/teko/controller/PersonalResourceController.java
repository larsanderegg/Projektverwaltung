package ch.lan.teko.controller;
import ch.lan.teko.model.PersonalResource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/personalresources")
@Controller
@RooWebScaffold(path = "personalresources", formBackingObject = PersonalResource.class)
public class PersonalResourceController {
}
