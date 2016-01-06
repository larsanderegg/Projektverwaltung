package ch.lan.teko.controller;
import ch.lan.teko.model.DocumentReference;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/documentreferences")
@Controller
@RooWebScaffold(path = "documentreferences", formBackingObject = DocumentReference.class)
public class DocumentReferenceController {
}
