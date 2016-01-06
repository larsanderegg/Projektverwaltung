package ch.lan.teko.controller;
import ch.lan.teko.model.Resource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/resources")
@Controller
@RooWebScaffold(path = "resources", formBackingObject = Resource.class)
public class ResourceController {
}
