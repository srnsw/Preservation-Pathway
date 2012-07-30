package au.gov.nsw.records.digitalarchive.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import au.gov.nsw.records.digitalarchive.model.Pathway;

@RequestMapping("/lookup")
@Controller
public class LookupController {

	  @RequestMapping(value = "/fmt/{id}", produces = "text/html")
	  public String findByPuid(@PathVariable("id") Long id, Model uiModel) {
	      uiModel.addAttribute("pathways", Pathway.findByPuid("fmt/" + id).getResultList());
	      return "pathways/list";
	  }
   
	  @RequestMapping(value = "/fmt/{id}/{purpose}", produces = "text/html")
	  public String findByPuidWithPurpose(@PathVariable("id") Long id, @PathVariable("purpose") String purpose, Model uiModel) {
	      uiModel.addAttribute("pathways", Pathway.findByPuidAndPurpose("fmt/" + id, purpose).getResultList());
	      return "pathways/list";
	  }
	  
    @RequestMapping
    public String index() {
        return "lookup/index";
    }
    
    @RequestMapping(params = {"find=ByPuid"}, method = RequestMethod.GET)
    public String findPathwaysByInput_format_descLikeAndTarget_format_descLikeForm(@RequestParam("puid") String input_format, Model model) {
        return String.format("redirect:/lookup/%s", input_format);
    }
}
