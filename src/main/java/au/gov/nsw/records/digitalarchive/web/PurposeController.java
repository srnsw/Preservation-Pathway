package au.gov.nsw.records.digitalarchive.web;

import au.gov.nsw.records.digitalarchive.model.Purpose;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/purposes")
@Controller
@RooWebScaffold(path = "purposes", formBackingObject = Purpose.class)
public class PurposeController {
}
