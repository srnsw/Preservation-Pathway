package au.gov.nsw.records.digitalarchive.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import au.gov.nsw.records.digitalarchive.model.Pathway;

@RequestMapping("/pathways")
@Controller
@RooWebScaffold(path = "pathways", formBackingObject = Pathway.class)
public class PathwayController {
	
	@Secured("ROLE_ADMIN")
  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid Pathway pathway, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
            
      Pathway oldPathway = Pathway.findPathway(Long.parseLong(httpServletRequest.getParameter("id")));
      
      if (pathway.getDeprecated() != oldPathway.getDeprecated()){
      	pathway.setDeprecated_by(SecurityContextHolder.getContext().getAuthentication().getName());
      	pathway.setDeprecation_date(new Date());
      }
      
    	// back store all essential attributes
      pathway.setAuthor(oldPathway.getAuthor());
      pathway.setCreation_date(oldPathway.getCreation_date());
      pathway.setPreservation_tool(oldPathway.getPreservation_tool());
      pathway.setPreservation_tool_desc(oldPathway.getPreservation_tool_desc());
      pathway.setInput_format(oldPathway.getInput_format());
      pathway.setInput_format_desc(oldPathway.getInput_format_desc());
      pathway.setPurpose(oldPathway.getPurpose());
      pathway.setTarget_format(oldPathway.getTarget_format());
      pathway.setTarget_format_desc(oldPathway.getTarget_format_desc());
      uiModel.asMap().clear();
      pathway.merge();
      return "redirect:/pathways/" + encodeUrlPathSegment(pathway.getId().toString(), httpServletRequest);
  }
	
	@Secured("ROLE_ADMIN")
  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Long id, Model uiModel) {
      populateEditForm(uiModel, Pathway.findPathway(id));
      return "pathways/update";
  }
  
	@Secured("ROLE_ADMIN")
  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid Pathway pathway, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
      if (bindingResult.hasErrors()) {
      	  uiModel.addAttribute("errors", "error occured");
          populateEditForm(uiModel, pathway);
          return "pathways/create";
      }
      
      // TODO
      //System.setProperty("http.proxyHost","proxywest");
  		//System.setProperty("http.proxyPort","8080");
  		
      uiModel.asMap().clear();
      
      // automatic populate the logged in user as the author
      pathway.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
      
      String baseUrl = "http://www.nationalarchives.gov.uk/pronom/";
      Document document;
			try {
	      // automatic populate description from PRONOM
				document = Jsoup.connect(baseUrl + pathway.getInput_format()).timeout(10*1000).get();
				String input_formatDesc = document.select(".topdata + td").first().text();
	      pathway.setInput_format_desc(input_formatDesc);
	      
	      document = Jsoup.connect(baseUrl + pathway.getPreservation_tool()).timeout(10*1000).get();
	      String toolsDesc = document.select(".data + td").first().text();
	      pathway.setPreservation_tool_desc(toolsDesc);
	      
	      document = Jsoup.connect(baseUrl + pathway.getTarget_format()).timeout(10*1000).get();
	      String targetDesc = document.select(".topdata + td").first().text();
	      pathway.setTarget_format_desc(targetDesc);
	      
			} catch (Exception e) { 
				uiModel.addAttribute("errors", "PUID does not exist");
				e.printStackTrace();
			  populateEditForm(uiModel, pathway);
        return "pathways/create";
			}
			
      pathway.persist();
      return "redirect:/pathways/" + encodeUrlPathSegment(pathway.getId().toString(), httpServletRequest);
  }
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
      Pathway pathway = Pathway.findPathway(id);
      pathway.remove();
      uiModel.asMap().clear();
      uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
      uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
      return "redirect:/pathways";
  }
	
	@RequestMapping(params = { "find=ByInput_format_descLikeAndTarget_format_descLike", "form" }, method = RequestMethod.GET)
  public String findPathwaysByInput_format_descLikeAndTarget_format_descLikeForm(Model model) {
      return "pathways/findPathwaysByInput_format_descLikeAndTarget_format_descLike";
  }
	
	 @RequestMapping(params = "find=ByInput_format_descLikeAndTarget_format_descLike", method = RequestMethod.GET)
   public String findPathwaysByInput_format_descLikeAndTarget_format_descLikeForm(@RequestParam("input_format_desc") String input_format_desc, @RequestParam("target_format_desc") String target_format_desc, Model model) {
       model.addAttribute("pathways", Pathway.findPathwaysByInput_format_descLikeAndTarget_format_descLike(input_format_desc, target_format_desc).getResultList());
       return "pathways/list";
   }
}
